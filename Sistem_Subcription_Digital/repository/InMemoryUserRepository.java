package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository{
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Map<Long, User> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findById(Long id) {
        if(id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if(email == null) {
            return Optional.empty();
        }

        return storage.values().stream().filter(s -> s.getEmail() != null && s.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public void save(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User is null");
        }

        if(user.getId() == null) {
            Long id = idSeq.incrementAndGet();

            user.setIdForRepository(id);

            storage.put(id, user);
        } else {
            storage.put(user.getId(), user);
        }
    }
    
}
