package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.PlanRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    public void deprecatePlan(Long planId, LocalDate effectiveDate, boolean allowNewSubscriptions) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        if(effectiveDate == null) {
            throw new IllegalArgumentException("Effective date cannot be null");
        }

        if(effectiveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Effective date cannot be in the past");
        }

        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalStateException("Plan is not found"));

        if(plan.isDeprecated()) {
            throw new IllegalStateException("Plan is already deprecated");
        }

        plan.setDeprecated(true, effectiveDate, allowNewSubscriptions);

        // Log deprecation (optional)
        // System.out.println("Plan " + plan.getId() + " deprecated effective from " + effectiveDate + ", allowNewSubscriptions: " + allowNewSubscriptions);

        planRepository.save(plan);
    }

    // public Plan archivePlan(Long planId) {}

    public Plan archivePlan(Long planId) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        Plan plan = planRepository.findById(planId).orElseThrow(() -> new IllegalStateException("Plan is not found"));

        if(plan.isArchived()) {
            throw new IllegalStateException("Plan is already archived");
        }

        plan.setArchived(true);

        // Log archive (optional)
        // System.out.println("Plan " + plan.getId() + " archived at " + plan.getArchiveDate());

        planRepository.save(plan);

        return plan;
    }

    // public Plan validatePlanForSubscription(Plan plan) {}

    public Plan validatePlanForSubscription(Plan plan) {
        if(plan == null) {
            throw new IllegalArgumentException("Plan is null");
        }

        if(plan.getId() == null) {
            throw new IllegalStateException("Plan ID is null");
        }

        Plan existingPlan = planRepository.findById(plan.getId())
                .orElseThrow(() -> new IllegalStateException("Plan is not found in repository"));

        if(existingPlan.isArchived()) {
            throw new IllegalStateException("Cannot subscribe to archived plan");
        }

        if(existingPlan.isDeprecated() && !existingPlan.isAllowNewSubscriptions()) {
            throw new IllegalStateException("New subscriptions not allowed for deprecated plan");
        }

        if(!existingPlan.isVisible()) {
            throw new IllegalStateException("Plan is not visible for subscription");
        }

        return existingPlan;
    }

    // public Plan migrateSubscribers(Long fromPlanId, Long toPlanId, LocalDate effectiveDate, boolean autoInvoice) {}

    public Plan migrateSubscribers(Long fromPlanId, Long toPlanId, LocalDate effectiveDate, boolean autoInvoice) {
        if(fromPlanId == null) {
            throw new IllegalArgumentException("From plan ID is null");
        }

        if(toPlanId == null) {
            throw new IllegalArgumentException("To plan ID is null");
        }

        if(fromPlanId.equals(toPlanId)) {
            throw new IllegalArgumentException("From and To plan IDs cannot be the same");
        }

        if(effectiveDate == null) {
            throw new IllegalArgumentException("Effective date cannot be null");
        }

        if(effectiveDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Effective date cannot be in the past");
        }

        planRepository.findById(fromPlanId)
                .orElseThrow(() -> new IllegalStateException("Source plan is not found"));

        Plan toPlan = planRepository.findById(toPlanId)
                .orElseThrow(() -> new IllegalStateException("Destination plan is not found"));

        if(toPlan.isArchived()) {
            throw new IllegalStateException("Cannot migrate to archived plan");
        }

        // Log migration (optional)
        // System.out.println("Migrating subscribers from plan " + fromPlanId + " to plan " + toPlanId + 
        //                   " effective from " + effectiveDate + ", autoInvoice: " + autoInvoice);

        return toPlan;
    }

    // public void setPlanVisibility(Long planId, boolean visible) {}

    public void setPlanVisibility(Long planId, boolean visible) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalStateException("Plan is not found"));

        if(plan.isArchived()) {
            throw new IllegalStateException("Cannot change visibility of archived plan");
        }

        plan.setVisibility(visible);

        // Log visibility change (optional)
        // System.out.println("Plan " + plan.getId() + " visibility set to " + visible);

        planRepository.save(plan);
    }

    // public List<Plan> listPriceHistory(Long planId) {}

    public List<Double> listPriceHistory(Long planId) {
        if(planId == null) {
            throw new IllegalArgumentException("Plan ID is null");
        }

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalStateException("Plan is not found"));

        return plan.getPriceHistory();
    }

    // public boolean canDowngradeMidCycle(Plan plan) {}

    public boolean canDowngradeMidCycle(Plan plan) {
        if(plan == null) {
            throw new IllegalArgumentException("Plan is null");
        }

        return plan.getAllowDowngradeMidCycle();
    }
}