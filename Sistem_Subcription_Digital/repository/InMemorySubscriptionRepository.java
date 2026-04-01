package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Subscription;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemorySubscriptionRepository implements SubscriptionRepository{
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Map<Long, Subscription> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Subscription> findById(Long id) {
        if(id == null) return Optional.empty();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Subscription> findByUserId(Long userId) {
        if (userId == null) return List.of();
        return storage.values().stream().filter(s -> s.getUser() != null && Objects.equals(s.getUser().getId(), userId)).collect(Collectors.toList());
    }

    @Override
    public Optional<Subscription> findActiveByUserAndPlan(Long userId, Long planId) {
        if(userId == null || planId == null) return Optional.empty();
        return storage.values().stream()
        .filter(s -> s.getUser() != null && userId.equals(s.getUser().getId()))
        .filter(s -> s.getPlan() != null && planId.equals(s.getPlan().getId()))
        .filter(s -> s.getStatus() == Subscription.SubscriptionStatus.ACTIVE)
        .findFirst();
    }

    @Override
    public void save(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("subscription is null");
        }

        if(subscription.getId() == null) {
            Long id = idSeq.incrementAndGet();

            subscription.setIdForRepository(id);
            storage.put(id, subscription);
        } else {
            storage.put(subscription.getId(), subscription);
        }
    }

    @Override
    public List<Subscription> findExpiringBefore(LocalDate date) {
        if(date == null) return List.of();
        return storage.values().stream()
        .filter(s -> s.getEndDate() != null)
        .filter(s -> s.getEndDate().toLocalDate().isBefore(date) || s.getEndDate().toLocalDate().isEqual(date))
        .collect(Collectors.toList());
    }

}
