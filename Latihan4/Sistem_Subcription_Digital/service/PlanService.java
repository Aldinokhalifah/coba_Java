package Sistem_Subcription_Digital.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.PlanRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
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

    // public Plan createPlan(Plan plan) {}

    // public Plan updatePlan(Long planId, Plan patch) {}

    // public Plan changePlanPrice(Long planId, BigDecimal newPrice, LocalDate effectiveDate) {}

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