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
import java.util.List;
import java.util.Optional;

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

        Subscription subscription = invoice.getSubscription();

        if( invoice.getStatus() == Invoice.InvoiceStatus.PAID ||
            invoice.getStatus() == Invoice.InvoiceStatus.EXPIRED) {
            throw new IllegalStateException("Invoice already paid or expired");
        }

        if (paymentStatus == Payment.PaymentStatus.SUCCESS && providerTransactionId == null) {
            throw new IllegalArgumentException("Missing provider transaction id for successful payment");
        }

        if (!invoice.isPayable(LocalDateTime.now())) {
            throw new IllegalStateException("Invoice not payable");
        }

        Payment newPayment = Payment.attempt(invoice, invoice.getAmount(), Payment.PaymentMethod.MANUAL);

        if(newPayment.getAmount() < 1) {
            throw new IllegalStateException("Amount less than 1");
        }

        if(paymentStatus == Payment.PaymentStatus.SUCCESS) {
            newPayment.complete(providerTransactionId, providerResponse, LocalDateTime.now());
            
            invoice.markPaid(LocalDateTime.now());

            subscription.onInvoicePaid(invoice);;
        } else {
            newPayment.fail(providerResponse);

            invoice.markFailed(providerResponse);

            subscription.onInvoiceFailed(invoice);
        }
        
        paymentRepository.save(newPayment);
        invoiceRepository.save(invoice);
        subscriptionRepository.save(subscription);
    }

    public void runPeriodicBilling(LocalDate billingDate) {
        if (billingDate == null) {
            throw new IllegalArgumentException("Billing date is null");
        }

        List<Subscription> subscriptions = subscriptionRepository.findExpiringBefore(billingDate);

        for (Subscription s : subscriptions) {
            // skip final states
            if (s.getStatus() == Subscription.SubscriptionStatus.CANCELLED ||
                s.getStatus() == Subscription.SubscriptionStatus.EXPIRED) {
                continue;
            }

            // auto renew mati → expire
            if (!s.getAutoRenew()) {
                s.expire();
                subscriptionRepository.save(s);
                continue;
            }

            // auto renew hidup
            // cek invoice lama
            if (invoiceRepository.findUnpaidBySubscription(s.getId()).isPresent()) {
                s.suspend();
                subscriptionRepository.save(s);
                continue;
            }

            Invoice renewalInvoice = new Invoice(
                null,
                s,
                billingDate.atStartOfDay(),
                billingDate.plusDays(7).atStartOfDay(),
                s.getPlan().getPricePeriod(),
                Invoice.InvoiceStatus.PENDING,
                null,
                0,
                null
            );

            invoiceRepository.save(renewalInvoice);
        }
    }

    public Optional<Subscription> getById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }

    public List<Subscription> getByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
}