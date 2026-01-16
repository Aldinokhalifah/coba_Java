package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Plan;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPlanRepository implements PlanRepository{
    private final AtomicLong idSeq = new AtomicLong(0);
    private final Map<Long, Plan> storage = new ConcurrentHashMap<>();


    @Override
    public Optional<Plan> findById(Long id) {
        if(id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Plan> findByCode(String code) {
        if(code == null) {
            return Optional.empty();
        }

        return storage.values().stream().filter(s -> s.getCode() != null && s.getCode().equalsIgnoreCase(code)).findFirst();
    }

    @Override
    public List<Plan> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Plan plan) {
        if(plan == null) {
            throw new IllegalArgumentException("Plan is null");
        }

        if(plan.getCode() == null) {
            throw new IllegalArgumentException("Code is null");
        }

        if(plan.getPricePeriod() <= 0) {
            throw new IllegalArgumentException("Price period must be greater than 0");
        }

        if(plan.getPeriod() == null) {
            throw new IllegalArgumentException("Period is null");
        }

        if(plan.getId() == null) {
            Long id = idSeq.incrementAndGet();

            plan.setIdForRepository(id);

            storage.put(id, plan);
        } else {
            storage.put(plan.getId(), plan);
        }
    }
    
}
