package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Invoice.InvoiceStatus;
import Sistem_Subcription_Digital.model.Payment;
import Sistem_Subcription_Digital.model.Plan;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public class InvoiceService {
    InvoiceRepository invoiceRepository;
    PaymentRepository paymentRepository;
    SubscriptionRepository subscriptionRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, SubscriptionRepository subscriptionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
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
    
    public Invoice createInitialInvoice(Subscription subscription) {
        if(subscription == null) {
            throw new IllegalArgumentException("Subscription is null");
        }

        if(subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Subscripition is not active");
        }

        if(subscription.getPlan() == null) {
            throw new IllegalArgumentException("Subscription has no plan");
        }

        if(subscription.getPlan().getPricePeriod() < 0) {
            throw new IllegalStateException("Invalid plan price");
        }

        if(subscription.getStartDate() == null) {
            throw new IllegalArgumentException("Invalid start date");
        }

        if(invoiceRepository.findUnpaidBySubscription(subscription.getId()).isPresent()) {
            throw new IllegalStateException("Unpaid invoice already exists");
        }

        LocalDateTime issueDate = LocalDateTime.now().isAfter(subscription.getStartDate()) ? LocalDateTime.now() : subscription.getStartDate();

        Invoice invoice = new Invoice(
            null,
            subscription, 
            issueDate, 
            LocalDateTime.now().plusDays(7), 
            subscription.getPlan().getPricePeriod(), 
            Invoice.InvoiceStatus.PENDING, 
            null, 
            0, 
            "No Failure"
        );
        
        invoiceRepository.save(invoice);

        return invoice;
    }

    public Invoice createRenewalInvoice(Subscription subscription, LocalDate billingDate) {
        if(subscription == null) {
            throw new IllegalArgumentException("Subscription is null");
        }

        if(subscription.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is null");
        }

        if(subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Subscripition is not active");
        }

        if(subscription.getPlan() == null) {
            throw new IllegalArgumentException("Subscription has no plan");
        }

        if(billingDate == null) {
            throw new IllegalArgumentException("Billing date is null");
        }

        if(billingDate.isAfter(LocalDateTime.now().toLocalDate())) {
            throw new IllegalArgumentException("Billing date expired");
        }

        if(billingDate.isBefore(subscription.getStartDate().toLocalDate())) {
            throw new IllegalArgumentException("Invalid billing date");
        }

        if(!invoiceRepository.existsBySubscriptionAndBillingDate(subscription.getId(), billingDate).isEmpty()) {
            throw new IllegalStateException("Duplicate invoice");
        }

        Optional<Invoice> last = invoiceRepository.findLatestBySubscriptionId(subscription.getId());

        if(last.isPresent() && last.get().getInvoiceDate().toLocalDate().isAfter(billingDate)) {
            throw new IllegalStateException("Billing date older than last invoice");
        }

        if(last.isPresent() && last.get().getStatus() == Invoice.InvoiceStatus.PENDING) {
            throw new IllegalStateException("Previous invoice still unpaid");
        }

        if(subscription.getEndDate() != null && subscription.getEndDate().toLocalDate().isBefore(billingDate)) {
            throw new IllegalStateException("Subscription already ended");
        }

        Period period = Period.between(subscription.getStartDate().toLocalDate(), billingDate);
        int totalMonths = period.getYears() * 12 + period.getMonths();
        int cycleLength = subscription.getPlan().getPeriod() == Plan.Period.MONTHLY ? 1 : 12;

        if(totalMonths % cycleLength != 0) {
            throw new IllegalStateException("Billing date not aligned to cycle");
        }

        double amount = subscription.getPlan().getPricePeriod();

        if(amount <= 0) throw new IllegalStateException("Invalid price");

        LocalDateTime issueAt = billingDate.atStartOfDay();
        LocalDateTime dueAt = issueAt.plusDays(7); 

        if(billingDate.isBefore(LocalDate.now().minusMonths(3))) {
            throw new IllegalStateException("Billing date too old");
        }

        Invoice invoice = new Invoice(
            null,
            subscription,
            issueAt,
            dueAt,
            amount,
            Invoice.InvoiceStatus.PENDING,
            null,
            0,
            null
        );

        invoiceRepository.save(invoice);

        return invoice;
    }

    public Invoice createUpgradeInvoice(Subscription subscription, Plan newPlan, LocalDate effectiveDate) {
        if(subscription == null) {
            throw new IllegalArgumentException("Subsciption is null");
        }

        if(newPlan == null) {
            throw new IllegalArgumentException("New plan is null");
        }

        if(subscription.getPlan().getId().equals(newPlan.getId())) {
            throw new IllegalArgumentException("Plan ID must be different from subscription ID");
        }

        if(subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            throw new IllegalArgumentException("Only active subscription can be upgraded");
        }

        if(effectiveDate == null) {
            throw new IllegalArgumentException("Effective date is null");
        }

        if(effectiveDate.isBefore(LocalDateTime.now().toLocalDate())) {
            throw new IllegalArgumentException("Invalid effective date");
        }

        if(newPlan.getPricePeriod() <= 0) {
            throw new IllegalArgumentException("Invalid new plan price");
        }

        invoiceRepository.findUnpaidBySubscription(subscription.getId())
        .ifPresent(s -> {
            s.markFailed("Subscription upgraded");
            invoiceRepository.save(s);
        });

        subscription.upgradeTo(newPlan, effectiveDate);
        subscriptionRepository.save(subscription);

        double amount = newPlan.getPricePeriod();

        LocalDateTime issueAt = LocalDateTime.now();
        LocalDateTime dueAt = issueAt.plusDays(7);

        Invoice invoice = new Invoice(
            null,
            subscription,
            issueAt,
            dueAt,
            amount,
            Invoice.InvoiceStatus.PENDING,
            null,
            0,
            "Upgrade plan " + " to " + newPlan.getId()
        );

        invoiceRepository.save(invoice);

        return invoice;
    }

    public Invoice expireInvoice(Long invoiceId, String reason) {
        if(invoiceId == null) {
            throw new IllegalArgumentException("Invoice ID is null");
        }

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow( () -> new IllegalArgumentException("Invoice not found"));

        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Expire reason is required");

        if(invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot expire paid invoice");
        }

        if (invoice.getStatus() == InvoiceStatus.EXPIRED) {
            return invoice;
        }

        if(invoice.getDueDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Invoice not overdue yet");
        }

        invoice.expire();

        Subscription subscription = invoice.getSubscription();

        if(subscription != null && subscription.getStatus() == Subscription.SubscriptionStatus.ACTIVE) {
            subscription.suspend();
            subscriptionRepository.save(subscription);
        }

        invoiceRepository.save(invoice);

        return invoice;
    }

    public Invoice markInvoicePaid(Long invoiceId, LocalDateTime paidAt) {
        if(invoiceId == null) {
            throw new IllegalArgumentException("Invoice ID is null");
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow( () -> new IllegalStateException("Invoice not found"));

        if(paidAt == null) {
            paidAt = LocalDateTime.now();
        }

        if(invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice already paid");
        }

        if(invoice.getStatus() == InvoiceStatus.EXPIRED) {
            throw new IllegalStateException("Cannot pay expired invoice");
        }

        if (invoice.getStatus() == InvoiceStatus.FAILED) {
            throw new IllegalStateException("Cannot pay failed invoice");
        }

        if(paidAt.isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("Paid time cannot be in the future");
        }

        invoice.markPaid(paidAt);

        Subscription subscription = invoice.getSubscription();

        if(subscription != null) {
            subscription.onInvoicePaid(invoice);
            subscriptionRepository.save(subscription);
        }

        invoiceRepository.save(invoice);

        return invoice;
    }

    public Invoice markInvoiceFailed(Long invoiceId, String reason) {
        if(invoiceId == null) {
            throw new IllegalArgumentException("Invoice ID is null");
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow( () -> new IllegalStateException("Invoice not found"));

        Subscription subscription = invoice.getSubscription();

        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Failed reason is required");

        if (invoice.getStatus() == InvoiceStatus.FAILED) {
            throw new IllegalStateException("Invoice already failed");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice already paid");
        }

        if (invoice.getStatus() == InvoiceStatus.EXPIRED) {
            throw new IllegalStateException("Invoice already expired");
        }

        invoice.markFailed(reason);
        invoice.incrementAttempt();

        int MAX_RETRY = 3;

        if(invoice.getAttemptCount() >= MAX_RETRY) {
            invoice.expire();
            if(subscription != null) {
                subscription.suspend();
            }
        }


        invoiceRepository.save(invoice);

        if(subscription != null) {
            subscriptionRepository.save(subscription);
        }

        return invoice;
    }

    public Optional<Invoice> getPayableInvoiceBySubscription(Long subscriptionId) {
        if(subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID is null");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
        .orElseThrow(() -> new IllegalStateException("Subscription not found"));

        if(subscription.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
            return Optional.empty();
        }

        Optional<Invoice> invoiceOpt = invoiceRepository.findUnpaidBySubscription(subscriptionId);

        if(invoiceOpt.isEmpty()) {
            return Optional.empty();
        }

        Invoice invoice = invoiceOpt.get();

        if(invoice.getStatus() != InvoiceStatus.PENDING) {
            return Optional.empty();
        }

        if(invoice.getDueDate().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        if(invoice.getAmount() <= 0) {
            return Optional.empty();
        }

        List<Payment> pendingPayment = paymentRepository.findPendingByInvoiceId(invoice.getId());

        if(!pendingPayment.isEmpty()) {
            return Optional.empty();
        }

        if(subscription.getEndDate() != null && subscription.getEndDate().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        Optional<Invoice> isLatestForSubscription = invoiceRepository.findLatestBySubscriptionId(subscriptionId);

        if(isLatestForSubscription.isEmpty()) return Optional.empty();

        if(!invoice.getId().equals(isLatestForSubscription.get().getId())) {
            return Optional.empty();
        }

        invoiceRepository.save(invoice);

        return Optional.of(invoice);
    }

    public boolean validateInvoicePayable(Invoice invoice, LocalDateTime now) {
        if(invoice == null) return false;
        if(invoice.getStatus() != InvoiceStatus.PENDING) return false;
        if(invoice.getDueDate().isBefore(now)) return false;
        if(invoice.getAmount() <= 0) return false;

        Subscription s = invoice.getSubscription();
        if(s == null || s.getStatus() != Subscription.SubscriptionStatus.ACTIVE) return false;
        if(s.getEndDate() != null && s.getEndDate().isBefore(now)) return false;

        if(!paymentRepository.findPendingByInvoiceId(invoice.getId()).isEmpty()) return false;

        Optional<Invoice> latest = invoiceRepository.findLatestBySubscriptionId(s.getId());
        return !(latest.isPresent() && !latest.get().getId().equals(invoice.getId()));
    }

    public void expireOverdueInvoices(LocalDate now) {
        if(now == null) {
            throw new IllegalArgumentException("Date is invalid");
        }

        List<Invoice> ovdInvoices = invoiceRepository.findOverdueInvoices(now);

        for(Invoice o : ovdInvoices) {
            if(o.getStatus() == InvoiceStatus.PAID || o.getStatus() == InvoiceStatus.EXPIRED || o.getStatus() == InvoiceStatus.FAILED) continue;

            o.expire();

            Subscription s = o.getSubscription();

            if(s != null && s.getStatus() == Subscription.SubscriptionStatus.ACTIVE) {
                s.suspend();
                subscriptionRepository.save(s);
            }

            invoiceRepository.save(o);
        }
    }
}