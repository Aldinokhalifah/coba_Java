import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class Sistem_Manajemen_Enrollment_Course {
    
    static class User {
        private Long id;
        private String name;
        private String email;
        private LocalDateTime createdAt;

        public User(Long id, String name, String email, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.createdAt = createdAt;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }

    static class Enrollment {
        enum Status{ACTIVE, CANCELLED};

        private Long id;
        private User user;
        private Sistem_Manajemen_Course.Course course;
        private Status status;
        private LocalDateTime enrolledAt;

        public Enrollment(Long id, User user, Sistem_Manajemen_Course.Course course, Status status, LocalDateTime enrolledAt) {
            this.id = id;
            this.user = user;
            this.course = course;
            this.status = status;
            this.enrolledAt = enrolledAt;
        }

        public Long getId() {
            return id;
        }

        public User getUser() {
            return user;
        }

        public Sistem_Manajemen_Course.Course getCourse() {
            return course;
        }

        public Status getStatus() {
            return status;
        }

        public LocalDateTime getEnrolledAt() {
            return enrolledAt;
        }

        public void cancel() {
            if(status != Status.ACTIVE) {
                throw new IllegalStateException("Enrollment sudah dibatalkan");
            }
            this.status = Status.CANCELLED;
        }
    }

    static interface EnrollmentRepository {
        Optional<Enrollment> findById(Long id);
        List<Enrollment> findByUserId(Long userId);
        Optional<Enrollment> findByUserAndCourse(Long userId, Long courseId);
        void save(Enrollment enrollment);
    }

    static interface UserRepository {
        Optional<User> findById(Long id);
    }

    static class InMemoryUserRepository implements UserRepository {
        private Map<Long, User> storage = new HashMap<>();

        @Override
        public Optional<User> findById(Long id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    static class InMemoryEnrollmentRepository implements EnrollmentRepository {
        private Map<Long, Enrollment> storage = new HashMap<>();
        
        @Override
        public Optional<Enrollment> findById(Long id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public List<Enrollment> findByUserId(Long userId) {
            return storage.values().stream()
            .filter(s -> Objects.equals(s.getUser().getId(), userId)).toList();
        }

        @Override
        public Optional<Enrollment> findByUserAndCourse(Long userId, Long courseId) {
            return storage.values().stream()
            .filter(s -> Objects.equals(s.getUser().getId(), userId))
            .filter(s -> Objects.equals(s.getCourse().getId(), courseId)).findFirst();
        }

        @Override
        public void save(Enrollment enrollment) {
            storage.put(enrollment.getId(), enrollment);
        }
    }

    static class EnrollmentService {
        private AtomicLong seq;
        private EnrollmentRepository enrollmentRepository;
        private UserRepository userRepository;
        private Sistem_Manajemen_Course.CourseRepository courseRepository;

        public EnrollmentService(AtomicLong seq, EnrollmentRepository enrollmentRepository, UserRepository userRepository, Sistem_Manajemen_Course.CourseRepository courseRepository) {
            this.seq = seq;
            this.enrollmentRepository = enrollmentRepository;
            this.userRepository = userRepository;
            this.courseRepository = courseRepository;
        }

        public void enrollUser(Long userId, Long courseId) {
            Sistem_Manajemen_Course.Course course = courseRepository.findById(courseId).orElseThrow(() -> new NoSuchElementException("Course tidak ditemukan"));

            User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User tidak ditemukan"));

            if(course.getStatus() != Sistem_Manajemen_Course.Course.Status.PUBLISHED) {
                throw new IllegalStateException("Course harus published");
            }

            Optional<Enrollment> enroll = enrollmentRepository.findByUserAndCourse(userId, courseId);

            if (enroll.isPresent()) {
                if (enroll.get().getStatus() == Enrollment.Status.ACTIVE) {
                    throw new IllegalStateException("Tidak boleh enroll dua kali");
                }
                if (enroll.get().getStatus() == Enrollment.Status.CANCELLED) {
                    throw new IllegalStateException("Enrollment sudah dibatalkan");
                }
            }

            Enrollment newEnrollment = new Enrollment(seq.incrementAndGet(), user, course, Enrollment.Status.ACTIVE, LocalDateTime.now());

            enrollmentRepository.save(newEnrollment);
        }

        public void cancelEnrollment(Long enrollmentId) {
            Enrollment enroll = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Enroll tidak ditemukan"));

            enroll.cancel();

            enrollmentRepository.save(enroll);
        }

        public List<Enrollment> getActiveEnrollmentsByUser(Long userId) {
            List<Enrollment> enroll = enrollmentRepository.findByUserId(userId).stream().filter(e -> Objects.equals(e.getStatus(), Enrollment.Status.ACTIVE)).toList();

            return enroll;
        }
    }
}
