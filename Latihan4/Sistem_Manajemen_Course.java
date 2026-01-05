
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class Sistem_Manajemen_Course {
    
    public static class Course {
        enum Status{DRAFT, PUBLISHED, ARCHIVED};

        private Long id;
        private String title;
        private String description;
        private double price;
        private Status status;
        private LocalDateTime createdAt;

        public Course(Long id, String title, String description, double price, Status status, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.price = price;
            this.status = status;
            this.createdAt = createdAt;
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }

        public Status getStatus() {
            return status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void publish() {
            if (status != Status.DRAFT) {
                throw new IllegalStateException("Course hanya bisa dipublish dari DRAFT");
            }
            this.status = Status.PUBLISHED;
        }

        public void archive() {
            if (status != Status.PUBLISHED) {
                throw new IllegalStateException("Course hanya bisa diarchive dari PUBLISHED");
            }
            this.status = Status.ARCHIVED;
        }
    }

    public static interface  CourseRepository {
        Optional<Course> findById(Long id);
        List<Course> findAll();
        void save(Course course);
        void deleteById(Long id);
        List<Course> findByStatus(Course.Status status);
    }

    static class InMemoryCourseRepository implements CourseRepository {
        private final Map<Long, Course> storage = new HashMap<>();

        @Override
        public Optional<Course> findById(Long id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public List<Course> findAll() {
            return new ArrayList<>(storage.values());
        }

        @Override
        public void save(Course course) {
            storage.put(course.getId(), course);
        }

        @Override
        public void deleteById(Long id) {
            storage.remove(id);
        }   

        @Override
        public List<Course> findByStatus(Course.Status status) {
            return storage.values().stream()
                    .filter(c -> c.getStatus() == status)
                    .toList();
        }
    }

    static class CourseService {
        private final CourseRepository courseRepository;
        private final AtomicLong seq;

        public CourseService(CourseRepository courseRepository, AtomicLong seq) {
            this.courseRepository = courseRepository;
            this.seq = seq;
        }

        public Course createCourse(String title, String description, double price) {
            if(title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title tidak boleh kosong");
            }

            if(description.trim().isEmpty()) {
                throw new IllegalArgumentException("Deskripsi tidak boleh kosong");
            }

            if(price < 0) {
                throw new IllegalArgumentException("Harga tidak boleh kurang dari 0");
            }

            Course newCourse = new Course(seq.incrementAndGet(), title, description, price, Course.Status.DRAFT, LocalDateTime.now());

            courseRepository.save(newCourse);

            return newCourse;
        }

        public void publishCourse(Long courseId) {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new NoSuchElementException("Course tidak ditemukan"));

            if (course.getStatus() != Course.Status.DRAFT){
                throw new IllegalStateException("Status bukan draft");
            }

            course.publish();
            courseRepository.save(course);

        }

        public void archiveCourse(Long courseId) {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new NoSuchElementException("Course tidak ditemukan"));

            if (course.getStatus() != Course.Status.PUBLISHED){
                throw new IllegalStateException("Status bukan published");
            }

            course.archive();
            courseRepository.save(course);
        }

        public List<Course> getPublishedCourses() {
            List<Course> courses = courseRepository.findByStatus(Course.Status.PUBLISHED);

            return courses;
        }
    }
}