package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Subscription;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Optional<Subscription> findById(Long id);
    List<Subscription> findByUserId(Long userId);
    Optional<Subscription> findActiveByUserAndPlan(Long userId, Long planId);
    void save(Subscription subscription); 
    List<Subscription> findExpiringBefore(LocalDate date);
}
