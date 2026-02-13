package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Plan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;

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
    
    public Invoice createInitialInvoice(Subscription subscription) {}

    public Invoice createRenewalInvoice(Subscription subscription, LocalDate billingDate) {}

    public Invoice createUpgradeInvoice(Subscription subscription, Plan newPlan, LocalDate effectiveDate) {}

    public Invoice expireInvoice(Long invoiceId, String reason) {}

    public Invoice markInvoicePaid(Long invoiceId, LocalDateTime paidAt) {}

    public Invoice markInvoiceFailed(Long invoiceId, String reason) {}

    public Optional<Invoice> getPayableInvoiceBySubscription(Long subscriptionId) {}

    public boolean validateInvoicePayable(Invoice invoice, LocalDateTime now) {}

    public void expireOverdueInvoices(LocalDate now) {}
}
