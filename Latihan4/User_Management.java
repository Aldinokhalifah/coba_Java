
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class User_Management {
    
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

        public List<User> findAll() {
            return new ArrayList<>(users);
        }

        public boolean deleteById(Long id) {
            return users.removeIf(u -> Objects.equals(u.getId(), id));
        }
    }

    static class UserService {
        private final UserRepository repo;

        public UserService(UserRepository repo) {
            this.repo = repo;
        }

        public User register(String username, String email, User.Role role) {
            if(username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username tidak boleh kosong");
            }

            if(repo.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username sudah terpakai");
            }
            

            if(!email.trim().isEmpty() && email.contains("@")) {
                throw new IllegalArgumentException("Email tidak boleh kosong");
            }

            if(role == null) {
                throw new IllegalArgumentException("Role tidak valid");
            }

            User newUser = repo.save(username, email, role);
            System.out.println("User berhasil ditambahkan dengan username: " + username);

            return newUser;
        }

        public Optional<User> getUser(Long id) {
            if(repo.findById(id).isPresent()) {
                Optional<User> user = repo.findById(id);
                return user; 
            } else {
                throw new IllegalArgumentException("ID Tidak ditemukan");
            }
        }

        public Optional<User> suspendUser(Long id) {
            Optional<User> user = repo.findById(id);

            if(!user.isPresent()) {
                throw new IllegalArgumentException("ID tidak ditemukan");
            }

            if(Objects.equals(user.get().getId(), id) && user.get().getStatus() != User.Status.SUSPENDED) {
                user.get().setStatus(User.Status.SUSPENDED);
                System.out.println("User dengan ID: " + id + " berhasil disuspend");
                return user;
            } else {
                throw new IllegalStateException("User sudah disuspend dan tidak bisa disuspend lagi");
            }
        }

        public Optional<User> changeRole(Long id, User.Role newRole) {
            Optional<User> user = repo.findById(id);

            if(!user.isPresent()) {
                throw new IllegalArgumentException("ID tidak ditemukan");
            }

            if(Objects.equals(user.get().getId(), id) && user.get().getRole() != newRole) {
                user.get().setRole(newRole);
                System.out.println("Role user dengan ID: " + id + "berhasil dirubah");
                return user;
            } else {
                throw new IllegalArgumentException("Role tidak valid");
            }
        }

        public User deleteUser(Long id) {
            User user = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("ID tidak ditemukan"));

            repo.deleteById(id);

            System.out.println("User berhasil dihapus dengan ID: " + id);

            return user;
        }
    }
}
