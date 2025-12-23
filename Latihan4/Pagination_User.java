import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Pagination_User {
    
    static class User {
        enum Role{ADMIN, MEMBER};
        enum Status{ACTIVE, SUSPENDED};

        private Long id;
        private String username;
        private String email;
        private Role role;
        private Status status;
        private LocalDateTime createdAt, updatedAt;

        public User(Long id, String username, String email, Role role, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
        public Role getRole() {
            return role;
        }

        public Status getStatus() {
            return status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    static class UserRepository {
        private final ArrayList<User> users = new ArrayList<>();
        private final AtomicLong seq;

        public UserRepository(AtomicLong seq) {
            this.seq = seq;
        }

        public User save(String username, String email, User.Role role) {
            User newUser = new User(seq.incrementAndGet(), username, email, role, User.Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());

            users.add(newUser);
            return newUser;
        }

        public Optional<User> findById(Long id) {
            return users.stream().filter(u -> Objects.equals(u.getId(), id)).findFirst();
        }

        public Optional<User> findByUsername(String username) {
            return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        }

        public List<User> findActiveUsers() {
            return users.stream().filter(u -> u.getStatus() == User.Status.ACTIVE).collect(Collectors.toList());
        }

        public List<User> findByRole(User.Role role) {
            return users.stream().filter(u -> u.getRole() == role).collect(Collectors.toList());
        }

        public List<User> findByStatus(User.Status status) {
            return users.stream().filter(u -> u.getStatus() == status).collect(Collectors.toList());
        }

        public List<User> findAll() {
            return new ArrayList<>(users);
        }
    }

    static class UserService {
        private final UserRepository repo;

        public UserService(UserRepository repo) {
            this.repo = repo;
        }

        public List<User> getUsers(int page, int size) {

            if (page < 1) {
                throw new IllegalArgumentException("Halaman minimal 1");
            }

            if (size < 1) {
                throw new IllegalArgumentException("Jumlah data minimal 1");
            }

            List<User> users = repo.findAll();
            int totalData = users.size();

            int startIndex = (page - 1) * size;

            if (startIndex >= totalData) {
                return List.of();
            }

            int endIndex = Math.min(startIndex + size, totalData);

            return users.subList(startIndex, endIndex);
        }

        public List<User> getUsersByStatus(User.Status status, int page, int size) {
            if (page < 1) {
                throw new IllegalArgumentException("Halaman minimal 1");
            }

            if (size < 1) {
                throw new IllegalArgumentException("Jumlah data minimal 1");
            }

            if(status == null) {
                throw new IllegalArgumentException("Status tidak valid");
            }

            List<User> users = repo.findByStatus(status);
            int totalData = users.size();

            int startIndex = (page - 1) * size;

            if (startIndex >= totalData) {
                return List.of();
            }

            int endIndex = Math.min(startIndex + size, totalData);

            return users.subList(startIndex, endIndex);
        }

        public List<User> getActiveUsers(int page, int size) {
            if (page < 1) {
                throw new IllegalArgumentException("Halaman minimal 1");
            }

            if (size < 1) {
                throw new IllegalArgumentException("Jumlah data minimal 1");
            }

            List<User> users = repo.findActiveUsers();
            int totalData = users.size();

            int startIndex = (page - 1) * size;

            if (startIndex >= totalData) {
                return List.of();
            }

            int endIndex = Math.min(startIndex + size, totalData);

            return users.subList(startIndex, endIndex);
        }

        public List<User> sortUsers(int page, int size, String sortBy, String direction) {
            if (page < 1) {
                throw new IllegalArgumentException("Halaman minimal 1");
            }
            if (size < 1) {
                throw new IllegalArgumentException("Jumlah data minimal 1");
            }
            if (sortBy == null || sortBy.trim().isEmpty()) {
                throw new IllegalArgumentException("SortBy harus diisi");
            }
            if (direction == null || direction.trim().isEmpty()) {
                throw new IllegalArgumentException("Arah harus diisi");
            }

            String field = sortBy.trim().toLowerCase();
            String dir = direction.trim().toLowerCase();

            // Validasi direction
            if (!dir.equals("asc") && !dir.equals("desc")) {
                throw new IllegalArgumentException("Direction harus 'asc' atau 'desc'");
            }
            boolean ascending = dir.equals("asc");

            // Ambil semua data
            List<User> users = repo.findAll();

            // Lakukan sorting sesuai field
            Comparator<User> comparator;

            switch (field) {
                case "username":
                    comparator = Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "email":
                    comparator = Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "role":
                    comparator = Comparator.comparing(User::getRole);
                    break;
                case "status":
                    comparator = Comparator.comparing(User::getStatus);
                    break;
                case "createdat":
                case "created_at":
                    comparator = Comparator.comparing(User::getCreatedAt);
                    break;
                case "updatedat":
                case "updated_at":
                    comparator = Comparator.comparing(User::getUpdatedAt);
                    break;
                default:
                    throw new IllegalArgumentException("SortBy tidak valid. Pilihan: username, email, role, status, createdAt, updatedAt");
            }

            // Terapkan ascending/descending
            if (!ascending) {
                comparator = comparator.reversed();
            }

            // Sort data
            List<User> sortedUsers = users.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

            // Pagination
            int totalData = sortedUsers.size();
            int startIndex = (page - 1) * size;

            if (startIndex >= totalData) {
                return List.of(); // halaman kosong
            }

            int endIndex = Math.min(startIndex + size, totalData);

            return sortedUsers.subList(startIndex, endIndex);
        }

    }
}
