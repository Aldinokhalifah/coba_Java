// QuestHarianApp.java
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestHarianApp {

    /* -------------------------
       Utilities & Exceptions
       ------------------------- */
    static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String msg) { super(msg); }
    }

    static class BusinessRuleException extends RuntimeException {
        public BusinessRuleException(String msg) { super(msg); }
    }

    /* -------------------------
       Entities
       ------------------------- */
    static class User {
        private Long id;
        private String username;
        private int level; // 0..100
        private boolean deleted = false;
        private LocalDateTime createdAt, updatedAt;

        public User(Long id, String username, int level, boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.username = username;
            this.level = level;
            this.deleted = deleted;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Convenience constructor for creation
        public User(String username, int level) {
            this(null, username, level, false, null, null);
        }

        // getters & setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public boolean isDeleted() { return deleted; }
        public void setDeleted(boolean deleted) { this.deleted = deleted; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public void softDelete() { this.deleted = true; this.updatedAt = LocalDateTime.now(); }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", level=" + level +
                    ", deleted=" + deleted +
                    ", createdAt=" + createdAt +
                    ", updatedAt=" + updatedAt +
                    '}';
        }
    }

    static class Quest {
        enum Difficulty { EASY, NORMAL, HARD }
        enum Status { ACTIVE, ARCHIVED }

        private Long id;
        private String title;
        private String description;
        private Difficulty difficulty;
        private int rewardExp;
        private Status status;
        private LocalDateTime createdAt, updatedAt;

        public Quest(Long id, String title, String description, Difficulty difficulty, int rewardExp, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.difficulty = difficulty;
            this.rewardExp = rewardExp;
            this.status = status;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Quest(String title, String description, Difficulty difficulty, int rewardExp) {
            this(null, title, description, difficulty, rewardExp, Status.ACTIVE, null, null);
        }

        // getters & setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Difficulty getDifficulty() { return difficulty; }
        public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
        public int getRewardExp() { return rewardExp; }
        public void setRewardExp(int rewardExp) { this.rewardExp = rewardExp; }
        public Status getStatus() { return status; }
        public void setStatus(Status status) { this.status = status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public void archive() { this.status = Status.ARCHIVED; this.updatedAt = LocalDateTime.now(); }

        @Override
        public String toString() {
            return "Quest{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", difficulty=" + difficulty +
                    ", rewardExp=" + rewardExp +
                    ", status=" + status +
                    ", createdAt=" + createdAt +
                    ", updatedAt=" + updatedAt +
                    '}';
        }
    }

    static class UserQuest {
        private Long id;
        private Long userId;
        private Long questId;
        private boolean isCompleted = false;
        private LocalDateTime assignedAt;
        private LocalDateTime completedAt;

        public UserQuest(Long id, Long userId, Long questId, boolean isCompleted, LocalDateTime assignedAt, LocalDateTime completedAt) {
            this.id = id;
            this.userId = userId;
            this.questId = questId;
            this.isCompleted = isCompleted;
            this.assignedAt = assignedAt;
            this.completedAt = completedAt;
        }

        public UserQuest(Long userId, Long questId) {
            this(null, userId, questId, false, LocalDateTime.now(), null);
        }

        // getters & setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getQuestId() { return questId; }
        public void setQuestId(Long questId) { this.questId = questId; }
        public boolean isCompleted() { return isCompleted; }
        public void setCompleted(boolean completed) { isCompleted = completed; }
        public LocalDateTime getAssignedAt() { return assignedAt; }
        public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
        public LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

        public void markComplete() {
            this.isCompleted = true;
            this.completedAt = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return "UserQuest{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", questId=" + questId +
                    ", isCompleted=" + isCompleted +
                    ", assignedAt=" + assignedAt +
                    ", completedAt=" + completedAt +
                    '}';
        }
    }

    /* -------------------------
       Repositories (in-memory)
       ------------------------- */
    static class UserRepository {
        private final List<User> users = new ArrayList<>();
        private final AtomicLong seq;

        public UserRepository(AtomicLong seq) { this.seq = seq; }

        public synchronized User save(User user) {
            if (user.getId() == null) {
                user.setId(seq.incrementAndGet());
                user.setCreatedAt(LocalDateTime.now());
                users.add(user);
                return user;
            } else {
                for (int i = 0; i < users.size(); i++) {
                    if (Objects.equals(users.get(i).getId(), user.getId())) {
                        user.setUpdatedAt(LocalDateTime.now());
                        users.set(i, user);
                        return user;
                    }
                }
                // fallback: treat as new
                user.setId(seq.incrementAndGet());
                if (user.getCreatedAt() == null) user.setCreatedAt(LocalDateTime.now());
                users.add(user);
                return user;
            }
        }

        public Optional<User> findById(Long id) {
            return users.stream().filter(u -> Objects.equals(u.getId(), id)).findFirst();
        }

        public Optional<User> findByUsername(String username) {
            return users.stream().filter(u -> username.equalsIgnoreCase(u.getUsername())).findFirst();
        }

        public List<User> findAll(boolean includeDeleted) {
            if (includeDeleted) return new ArrayList<>(users);
            return users.stream().filter(u -> !u.isDeleted()).collect(Collectors.toList());
        }

        public void softDelete(Long id) {
            findById(id).ifPresent(u -> {
                u.softDelete();
            });
        }
    }

    static class QuestRepository {
        private final List<Quest> quests = new ArrayList<>();
        private final AtomicLong seq;

        public QuestRepository(AtomicLong seq) { this.seq = seq; }

        public synchronized Quest save(Quest quest) {
            if (quest.getId() == null) {
                quest.setId(seq.incrementAndGet());
                quest.setCreatedAt(LocalDateTime.now());
                quests.add(quest);
                return quest;
            } else {
                for (int i = 0; i < quests.size(); i++) {
                    if (Objects.equals(quests.get(i).getId(), quest.getId())) {
                        quest.setUpdatedAt(LocalDateTime.now());
                        quests.set(i, quest);
                        return quest;
                    }
                }
                // fallback insert
                quest.setId(seq.incrementAndGet());
                if (quest.getCreatedAt() == null) quest.setCreatedAt(LocalDateTime.now());
                quests.add(quest);
                return quest;
            }
        }

        public Optional<Quest> findById(Long id) {
            return quests.stream().filter(q -> Objects.equals(q.getId(), id)).findFirst();
        }

        public List<Quest> findAll() {
            return new ArrayList<>(quests);
        }

        public List<Quest> findByDifficulty(Quest.Difficulty difficulty) {
            return quests.stream().filter(q -> q.getDifficulty() == difficulty).collect(Collectors.toList());
        }

        public List<Quest> findByStatus(Quest.Status status) {
            return quests.stream().filter(q -> q.getStatus() == status).collect(Collectors.toList());
        }

        public List<Quest> findByMinRewardExp(int minExp) {
            return quests.stream().filter(q -> q.getRewardExp() >= minExp).collect(Collectors.toList());
        }

        public void archive(Long id) {
            findById(id).ifPresent(Quest::archive);
        }
    }

    static class UserQuestRepository {
        private final List<UserQuest> userQuests = new ArrayList<>();
        private final AtomicLong seq;

        public UserQuestRepository(AtomicLong seq) { this.seq = seq; }

        public synchronized UserQuest save(UserQuest uq) {
            if (uq.getId() == null) {
                uq.setId(seq.incrementAndGet());
                uq.setAssignedAt(LocalDateTime.now());
                userQuests.add(uq);
                return uq;
            } else {
                for (int i = 0; i < userQuests.size(); i++) {
                    if (Objects.equals(userQuests.get(i).getId(), uq.getId())) {
                        userQuests.set(i, uq);
                        return uq;
                    }
                }
                uq.setId(seq.incrementAndGet());
                userQuests.add(uq);
                return uq;
            }
        }

        public Optional<UserQuest> findByUserIdAndQuestId(Long userId, Long questId) {
            return userQuests.stream()
                    .filter(uq -> Objects.equals(uq.getUserId(), userId) && Objects.equals(uq.getQuestId(), questId))
                    .findFirst();
        }

        public Optional<UserQuest> findById(Long id) {
            return userQuests.stream().filter(uq -> Objects.equals(uq.getId(), id)).findFirst();
        }

        public List<UserQuest> findByUserId(Long userId) {
            return userQuests.stream().filter(uq -> Objects.equals(uq.getUserId(), userId)).collect(Collectors.toList());
        }

        public List<UserQuest> findAll() {
            return new ArrayList<>(userQuests);
        }
    }

    /* -------------------------
    Services (business logic)
       ------------------------- */
    static class UserService {
        private final UserRepository repo;

        public UserService(UserRepository repo) { this.repo = repo; }

        public User createUser(String username, int level) {
            validateUsername(username);
            validateLevel(level);
            if (repo.findByUsername(username).isPresent()) {
                throw new BusinessRuleException("Username already exists");
            }
            User u = new User(username, level);
            return repo.save(u);
        }

        public User updateUser(Long id, Optional<String> usernameOpt, Optional<Integer> levelOpt) {
            User u = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            usernameOpt.ifPresent(name -> {
                validateUsername(name);
                // check uniqueness
                repo.findByUsername(name).ifPresent(existing -> {
                    if (!Objects.equals(existing.getId(), id)) throw new BusinessRuleException("Username already exists");
                });
                u.setUsername(name);
            });
            levelOpt.ifPresent(l -> {
                validateLevel(l);
                u.setLevel(l);
            });
            u.setUpdatedAt(LocalDateTime.now());
            return repo.save(u);
        }

        public User getUser(Long id) {
            return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        public List<User> listUsers() {
            return repo.findAll(false);
        }

        public void softDeleteUser(Long id) {
            User u = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            u.softDelete();
            repo.save(u);
        }

        private void validateUsername(String username) {
            if (username == null || username.trim().isEmpty()) throw new BusinessRuleException("Username empty");
        }

        private void validateLevel(int level) {
            if (level < 0 || level > 100) throw new BusinessRuleException("Level out of range");
        }
    }

    static class QuestService {
        private final QuestRepository repo;

        public QuestService(QuestRepository repo) { this.repo = repo; }

        public Quest createQuest(String title, String description, Quest.Difficulty difficulty, int rewardExp) {
            if (title == null || title.trim().isEmpty()) throw new BusinessRuleException("Title empty");
            if (rewardExp < 0) throw new BusinessRuleException("rewardExp must be >= 0");
            Quest q = new Quest(title, description, difficulty, rewardExp);
            return repo.save(q);
        }

        public Quest updateQuest(Long id, Optional<String> titleOpt, Optional<String> descriptionOpt, Optional<Integer> rewardExpOpt, Optional<Quest.Difficulty> difficultyOpt) {
            Quest q = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quest not found"));
            titleOpt.ifPresent(t -> { if(t.trim().isEmpty()) throw new BusinessRuleException("Title empty"); q.setTitle(t); });
            descriptionOpt.ifPresent(q::setDescription);
            rewardExpOpt.ifPresent(re -> { if (re < 0) throw new BusinessRuleException("rewardExp must be >=0"); q.setRewardExp(re);});
            difficultyOpt.ifPresent(q::setDifficulty);
            q.setUpdatedAt(LocalDateTime.now());
            return repo.save(q);
        }

        public Quest getQuest(Long id) {
            return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quest not found"));
        }

        public List<Quest> listQuests(Optional<Quest.Difficulty> difficultyOpt, Optional<Quest.Status> statusOpt, Optional<Integer> minRewardOpt) {
            Stream<Quest> stream = repo.findAll().stream();
            if (difficultyOpt.isPresent()) stream = stream.filter(q -> q.getDifficulty() == difficultyOpt.get());
            if (statusOpt.isPresent()) stream = stream.filter(q -> q.getStatus() == statusOpt.get());
            if (minRewardOpt.isPresent()) stream = stream.filter(q -> q.getRewardExp() >= minRewardOpt.get());
            return stream.collect(Collectors.toList());
        }

        public void archiveQuest(Long id) {
            Quest q = getQuest(id);
            if (q.getStatus() == Quest.Status.ARCHIVED) throw new BusinessRuleException("Already archived");
            q.archive();
            repo.save(q);
        }
    }

    static class UserQuestService {
        private final UserQuestRepository uqRepo;
        private final UserRepository userRepo;
        private final QuestRepository questRepo;

        public UserQuestService(UserQuestRepository uqRepo, UserRepository userRepo, QuestRepository questRepo) {
            this.uqRepo = uqRepo;
            this.userRepo = userRepo;
            this.questRepo = questRepo;
        }

        public UserQuest assignQuest(Long userId, Long questId) {
            User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (user.isDeleted()) throw new BusinessRuleException("User deleted");
            Quest quest = questRepo.findById(questId).orElseThrow(() -> new ResourceNotFoundException("Quest not found"));
            if (quest.getStatus() != Quest.Status.ACTIVE) throw new BusinessRuleException("Quest is archived");
            if (quest.getDifficulty() == Quest.Difficulty.HARD && user.getLevel() < 10) throw new BusinessRuleException("User level too low for HARD quest");
            if (uqRepo.findByUserIdAndQuestId(userId, questId).isPresent()) throw new BusinessRuleException("Quest already assigned to user");

            UserQuest uq = new UserQuest(userId, questId);
            return uqRepo.save(uq);
        }

        public UserQuest markCompleted(Long userId, Long questId) {
            UserQuest uq = uqRepo.findByUserIdAndQuestId(userId, questId)
                    .orElseThrow(() -> new ResourceNotFoundException("UserQuest not found"));
            if (uq.isCompleted()) throw new BusinessRuleException("Already completed");

            // Start pseudo-transaction
            Quest quest = questRepo.findById(questId).orElseThrow(() -> new ResourceNotFoundException("Quest not found"));
            User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

            uq.markComplete();
            uqRepo.save(uq);

            int inc = quest.getRewardExp() / 10;
            user.setLevel(Math.min(100, user.getLevel() + inc));
            userRepo.save(user);
            // End pseudo-transaction

            return uq;
        }

        public List<UserQuest> listUserQuests(Long userId, Optional<Boolean> completedOnlyOpt, Optional<Boolean> activeOnlyOpt, Optional<Quest.Difficulty> difficultyOpt) {
            List<UserQuest> list = uqRepo.findByUserId(userId);
            Stream<UserQuest> stream = list.stream();
            if (completedOnlyOpt.isPresent() && completedOnlyOpt.get()) stream = stream.filter(UserQuest::isCompleted);
            if (activeOnlyOpt.isPresent() && activeOnlyOpt.get()) {
                stream = stream.filter(uq -> {
                    Quest q = questRepo.findById(uq.getQuestId()).orElse(null);
                    return q != null && q.getStatus() == Quest.Status.ACTIVE;
                });
            }
            if (difficultyOpt.isPresent()) {
                stream = stream.filter(uq -> {
                    Quest q = questRepo.findById(uq.getQuestId()).orElse(null);
                    return q != null && q.getDifficulty() == difficultyOpt.get();
                });
            }
            return stream.collect(Collectors.toList());
        }
    }

    /* -------------------------
    Main - contoh usage
       ------------------------- */
    public static void main(String[] args) {
        // init id generators
        AtomicLong userSeq = new AtomicLong(0);
        AtomicLong questSeq = new AtomicLong(0);
        AtomicLong uqSeq = new AtomicLong(0);

        // repos
        UserRepository userRepo = new UserRepository(userSeq);
        QuestRepository questRepo = new QuestRepository(questSeq);
        UserQuestRepository uqRepo = new UserQuestRepository(uqSeq);

        // services
        UserService userService = new UserService(userRepo);
        QuestService questService = new QuestService(questRepo);
        UserQuestService uqService = new UserQuestService(uqRepo, userRepo, questRepo);

        // create users
        User u1 = userService.createUser("alice", 5);      // low level
        User u2 = userService.createUser("bob", 20);

        // create quests
        Quest q1 = questService.createQuest("Collect wood", "Kumpulkan 10 kayu", Quest.Difficulty.EASY, 50);
        Quest q2 = questService.createQuest("Slay wolf", "Bunuh serigala", Quest.Difficulty.NORMAL, 120);
        Quest q3 = questService.createQuest("Defeat dragon", "Basmi naga", Quest.Difficulty.HARD, 500);

        System.out.println("=== USERS ===");
        userService.listUsers().forEach(System.out::println);

        System.out.println("\n=== QUESTS ===");
        questService.listQuests(Optional.empty(), Optional.empty(), Optional.empty()).forEach(System.out::println);

        System.out.println("\n=== ASSIGN QUESTS ===");
        try {
            uqService.assignQuest(u1.getId(), q1.getId()); // should succeed
            uqService.assignQuest(u2.getId(), q3.getId()); // should succeed (bob level 20)
            // following will fail (alice level 5 can't take HARD)
            uqService.assignQuest(u1.getId(), q3.getId());
        } catch (Exception ex) {
            System.out.println("assign failed: " + ex.getMessage());
        }

        System.out.println("\n=== COMPLETE QUESTS ===");
        // complete q1 for alice
        uqService.markCompleted(u1.getId(), q1.getId());
        System.out.println("Alice after completion: " + userService.getUser(u1.getId()));

        // complete q3 for bob
        uqService.markCompleted(u2.getId(), q3.getId());
        System.out.println("Bob after completion: " + userService.getUser(u2.getId()));

        System.out.println("\n=== USER QUESTS FOR ALICE ===");
        uqRepo.findByUserId(u1.getId()).forEach(System.out::println);

        System.out.println("\n=== ARCHIVE QUEST q2 THEN TRY ASSIGN ===");
        questService.archiveQuest(q2.getId());
        try {
            uqService.assignQuest(u1.getId(), q2.getId()); // should fail (archived)
        } catch (Exception ex) {
            System.out.println("assign failed: " + ex.getMessage());
        }

        System.out.println("\n=== DONE ===");
    }
}
