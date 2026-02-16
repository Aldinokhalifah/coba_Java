package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Invoice.InvoiceStatus;
import Sistem_Subcription_Digital.model.Plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
import java.nio.channels.IllegalSelectorException;
import java.time.Period;
import java.util.List;

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

    // public Invoice createUpgradeInvoice(Subscription subscription, Plan newPlan, LocalDate effectiveDate) {}

    // public Invoice expireInvoice(Long invoiceId, String reason) {}

    // public Invoice markInvoicePaid(Long invoiceId, LocalDateTime paidAt) {}

    // public Invoice markInvoiceFailed(Long invoiceId, String reason) {}

    // public Optional<Invoice> getPayableInvoiceBySubscription(Long subscriptionId) {}

    // public boolean validateInvoicePayable(Invoice invoice, LocalDateTime now) {}

    // public void expireOverdueInvoices(LocalDate now) {}
}
