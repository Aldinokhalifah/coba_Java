package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Payment;
import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.model.User;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.PlanRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
import Sistem_Subcription_Digital.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SubscriptionService {
    SubscriptionRepository subscriptionRepository;
    UserRepository userRepository;
    PlanRepository planRepository;
    InvoiceRepository invoiceRepository;
    PaymentService paymentService;
    PaymentRepository paymentRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, PlanRepository planRepository, InvoiceRepository invoiceRepository, PaymentService paymentService, PaymentRepository paymentRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    public SubscriptionRepository getSubscriptionRepository() {
        return subscriptionRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PlanRepository getPlanRepository() {
        return planRepository;
    }

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public Subscription createSubscription(Long userId, Long planId, boolean autoRenew) {
        if (userId == null || planId == null) {
            throw new IllegalArgumentException("userId or planId is null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        // validasi: user tidak boleh punya subscription ACTIVE untuk plan yang sama
        subscriptionRepository
                .findActiveByUserAndPlan(userId, planId)
                .ifPresent(s -> {
                    throw new IllegalStateException("User already has active subscription for this plan");
                });

        // buat subscription baru (factory / constructor, BUKAN ambil dari repo)
        Subscription subscription = Subscription.create(
                user,
                plan,
                LocalDateTime.now(),
                plan.getPeriod()
        );

        subscriptionRepository.save(subscription);

        // buat invoice pertama
        Invoice invoice = new Invoice(
            null, 
            subscription,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7),
            plan.getPricePeriod(),
            Invoice.InvoiceStatus.PENDING,
            null, 
            0,    
            ""   
        );

        invoiceRepository.save(invoice);

        /*
            Payment flow (nanti):
            - paymentService.process(invoice)
            - if success:
                invoice.markPaid(now)
            - if fail:
                invoice.markFailed(reason)
                subscription.suspend()
        */

        return subscription;
    }

    public void cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription id not found"));

        if (subscription.getStatus() == Subscription.SubscriptionStatus.EXPIRED ||
            subscription.getStatus() == Subscription.SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription already ended");
        }

        subscription.cancel();

        invoiceRepository.findUnpaidBySubscription(subscription.getId())
                .ifPresent(invoice -> {
                    invoice.markFailed("Subscription cancelled by user");
                    invoiceRepository.save(invoice);
                });

        subscriptionRepository.save(subscription);
    }

    public Subscription upgradeSubscription(Long subscriptionId, Long newPlanId, LocalDate effectiveDate) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE subscription can be upgraded");
        }

        Plan newPlan = planRepository.findById(newPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        if (subscription.getPlan().getId().equals(newPlan.getId())) {
            throw new IllegalArgumentException("Cannot upgrade to same plan");
        }

        if(newPlan.getPricePeriod() <= subscription.getPlan().getPricePeriod()) {
            throw new IllegalStateException("New plan must be higher than current plan");
        }

        if (effectiveDate == null || effectiveDate.isBefore(subscription.getStartDate().toLocalDate())) {
            throw new IllegalArgumentException("Invalid effective date");
        }

        subscription.upgradeTo(newPlan, effectiveDate);
        
        subscriptionRepository.save(subscription);

        invoiceRepository.findUnpaidBySubscription(subscription.getId())
        .ifPresent(invoice -> {
            invoice.markFailed("Upgraded to new plan");
            invoiceRepository.save(invoice);
        });

        Invoice newInvoice = new Invoice(
            null,
            subscription,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7),
            newPlan.getPricePeriod(),
            Invoice.InvoiceStatus.PENDING,
            null, 
            0,    
            "" 
        );


        invoiceRepository.save(newInvoice);

        return subscription;
    }

    public Subscription downgradeSubscription(Long subscriptionId, Long newPlanId, LocalDate effectiveDate) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE subscription can be downgraded");
        }

        Plan newPlan = planRepository.findById(newPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        if (subscription.getPlan().getId().equals(newPlan.getId())) {
            throw new IllegalArgumentException("Cannot downgrade to same plan");
        }

        if(newPlan.getPricePeriod() >= subscription.getPlan().getPricePeriod()) {
            throw new IllegalStateException("New plan must be lower than current plan");
        }

        if (effectiveDate == null || !effectiveDate.equals(subscription.getEndDate().toLocalDate())) {
            throw new IllegalArgumentException("Effective date must be equal to subscription end date");
        }

        subscription.downgradeTo(newPlan, effectiveDate);

        invoiceRepository.findUnpaidBySubscription(subscription.getId())
        .ifPresent(invoice -> {
            invoice.markFailed("Subscription downgraded");
            invoiceRepository.save(invoice);
        });


        subscriptionRepository.save(subscription);

        return subscription;
    }

    public void handleInvoicePayment(Long invoiceId, Payment.PaymentStatus paymentStatus, String providerTransactionId, String providerResponse) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice id not found"));

        if( invoice.getStatus() == Invoice.InvoiceStatus.PAID ||
            invoice.getStatus() == Invoice.InvoiceStatus.EXPIRED) {
            throw new IllegalStateException("Invoice already paid or expired");
        }

        invoice.isPayable(LocalDateTime.now());

        Payment newPayment = Payment.attempt(invoice, invoice.getAmount(), Payment.PaymentMethod.MANUAL);

        if(newPayment.getAmount() < 1) {
            throw new IllegalStateException("Amount less than 1");
        }

        if(newPayment.getAmount() != invoice.getAmount()) {
            throw new IllegalStateException("Amount does not match");
        }

        if(newPayment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new IllegalStateException("Status is not pending");
        }

        newPayment.complete(providerTransactionId, providerResponse, LocalDateTime.now());

        paymentRepository.save(newPayment);

        invoice.markPaid(LocalDateTime.now());

        invoiceRepository.save(invoice);

        
    }

    // public void runPeriodicBilling(LocalDate billingDate) {
        
    // }
    // public Optional<Subscription> getById(Long subscriptionId) {
        
    // }
    // public List<Subscription> getByUserId(Long userId) {
        
    // }

}
