package Sistem_Subcription_Digital.repository;

import Sistem_Subcription_Digital.model.Plan;
import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    Optional<Plan> findById(Long id);

    Optional<Plan> findByCode(String code);

    List<Plan> findAll();

    void save(Plan plan);
}

