package Sistem_Subcription_Digital.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.PlanRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PlanService {
    PlanRepository planRepository;
    InvoiceRepository invoiceRepository;
    PaymentRepository paymentRepository;
    SubscriptionRepository subscriptionRepository;

    public PlanService(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, SubscriptionRepository subscriptionRepository, PlanRepository planRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public SubscriptionRepository getSubscriptionRepository() {
        return subscriptionRepository;
    }

    public PlanRepository getPlanRepository() {
        return planRepository;
    }

    public Plan createPlan(Plan plan) {
        if(plan == null) {
            throw new IllegalArgumentException("Plan is null");
        }

        if(plan.getName().isBlank()) {
            throw new IllegalStateException("Name is null");
        }

        List<Plan> existCode = planRepository.existByCode(plan.getCode());

        if(existCode.size() > 1) {
            throw new IllegalStateException("Code must be unique");
        }

        if(plan.getPricePeriod() == 0) {
            throw new IllegalStateException("Price is invalid");
        }

        if(plan.getPeriod() == null) {
            throw new IllegalStateException("Period is invalid");
        }

        planRepository.save(plan);

        return plan;
    }

    public Plan updatePlan(Long planId, Plan patch) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        if(patch == null) {
            throw new IllegalArgumentException("Patch is null");
        }

        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalStateException("Plan is not found"));

        if(!plan.getCode().equals(patch.getCode())) {
            throw new IllegalStateException("Cannot change plan code");
        }

        if(patch.getName() == null || patch.getName().isBlank()) {
            throw new IllegalStateException("Plan name cannot be blank");
        }

        if(patch.getPeriod() == null) {
            throw new IllegalStateException("Period cannot be null");
        }

        if(plan.getPricePeriod() != patch.getPricePeriod()) {
            throw new IllegalStateException("Price is invalid");
        }

        if(!plan.getName().equals(patch.getName())) {
            plan.setName(patch.getName());
        }

        planRepository.save(plan);

        return plan;
    }

    public Plan changePlanPrice(Long planId, BigDecimal newPrice, LocalDate effectiveDate) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        if(newPrice == null || newPrice.signum() <= 0) {
            throw new IllegalArgumentException("New price must be greater than 0");
        }

        if(effectiveDate == null) {
            throw new IllegalArgumentException("Effective date cannot be null");
        }

        LocalDate today = LocalDate.now();
        if(effectiveDate.isBefore(today)) {
            throw new IllegalArgumentException("Effective date cannot be in the past");
        }

        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalStateException("Plan is not found"));

        double currentPrice = plan.getPricePeriod();
        if(currentPrice != newPrice.doubleValue()) {
            plan.setPricePeriod(newPrice.doubleValue());            
        } else {
            throw new IllegalStateException("New price is same as current price");
        }

        planRepository.save(plan);

        return plan;
    }


    public Optional<Plan> getPlanById(Long planId) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        return planRepository.findById(planId).or(() -> {
            throw new IllegalStateException("Plan is not found");
        });
    }

    public Optional<Plan> getPlanByCode(String code) {
        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code is null");
        }

        return planRepository.findByCode(code).or(() -> {
            throw new IllegalStateException("Code is not found");
        });
    }

    public List<Plan> listPlans() {
        List<Plan> plans = planRepository.findAll();

        if(plans.isEmpty()) {
            return List.of();
        } else {
            return plans;
        }
    }

    // public void deprecatePlan(Long planId, LocalDate effectiveDate, boolean allowNewSubscriptions) {}

    // public Plan archivePlan(Long planId) {}

    // public Plan validatePlanForSubscription(Plan plan) {}

    // public Plan migrateSubscribers(Long fromPlanId, Long toPlanId, LocalDate effectiveDate, boolean autoInvoice) {}

    // public void setPlanVisibility(Long planId, boolean visible) {}

    // public List<Plan> listPriceHistory(Long planId) {}

    // public boolean canDowngradeMidCycle(Plan plan) {}
}