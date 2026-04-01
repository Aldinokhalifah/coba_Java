package Sistem_Subcription_Digital.service;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Invoice.InvoiceStatus;
import Sistem_Subcription_Digital.model.Payment;
import Sistem_Subcription_Digital.model.Payment.PaymentStatus;
import Sistem_Subcription_Digital.model.Subscription;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentService {
    PaymentRepository paymentRepository;
    InvoiceRepository invoiceRepository;
    SubscriptionRepository subscriptionRepository;

    public PaymentService(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository, SubscriptionRepository subscriptionRepository) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }

    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }

    public SubscriptionRepository getSubscriptionRepository() {
        return subscriptionRepository;
    }

    public Payment attemptPayment(Invoice invoice, Payment.PaymentMethod method) {
        if(invoice == null) {
            throw new IllegalArgumentException("Invoice is null");
        }

        if(method == null) {
            throw new IllegalArgumentException("Payment method is null");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice already paid");
        }

        if (invoice.getStatus() == InvoiceStatus.EXPIRED) {
            throw new IllegalStateException("Invoice expired");
        }

        if (!invoice.isPayable(LocalDateTime.now())) {
            throw new IllegalStateException("Invoice not payable");
        }

        if (invoice.getAmount() <= 0) {
            throw new IllegalStateException("Invoice amount invalid");
        }

        if (!paymentRepository.findByInvoiceId(invoice.getId()).isEmpty()) {
            throw new IllegalStateException("Unfinished payment already exists");
        }

        Payment payment = Payment.attempt(invoice, invoice.getAmount(), method);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment must start as PENDING");
        }

        paymentRepository.save(payment); 

        return payment;
    }

    public Payment processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment id not found"));

        if(payment.getStatus() != PaymentStatus.PENDING) {
            return payment;
        }

        Invoice invoice = payment.getInvoice();

        if (invoice == null) {
            throw new IllegalStateException("Payment has no invoice");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice already paid");
        }

        if (invoice.getStatus() == InvoiceStatus.EXPIRED) {
            payment.fail("Invoice expired");
            paymentRepository.save(payment);
            return payment;
        }

        if (payment.getAmount() != invoice.getAmount()) {
            throw new IllegalStateException("Payment amount mismatch");
        }

        if (payment.getProviderTransactionId() == null) {
            throw new IllegalStateException("Missing provider transaction id");
        }

        payment.complete(payment.getProviderTransactionId(), "Process complete", LocalDateTime.now());

        paymentRepository.save(payment);

        return payment;
    }

    public Payment processPaymentForInvoice(Long invoiceId, Payment.PaymentMethod method) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice id not found"));

        if(method == null) {
            throw new IllegalArgumentException("Payment method is null");
        }

        if(invoice.getStatus() != InvoiceStatus.PENDING) {
            throw new IllegalStateException("Invoice is not payable");
        }

        if (!invoice.isPayable(LocalDateTime.now())) {
            throw new IllegalStateException("Invoice expired");
        }

        List<Payment> existsPendingByInvoice = paymentRepository.findByInvoiceId(invoiceId);

        if(!existsPendingByInvoice.isEmpty()) {
            throw new IllegalStateException("There is already a pending payment for this invoice");
        }

        if (invoice.getAmount() < 1) {
            throw new IllegalStateException("Invalid invoice amount");
        }

        Payment payment = Payment.attempt(invoice, invoice.getAmount(), method);

        paymentRepository.save(payment);

        return processPayment(payment.getId());
    }

    public void handleGatewayCallback(Long paymentId, PaymentStatus gatewayStatus, String providerTxId, String providerResponse) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment id not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            return; // idempotent, gateway bisa callback berkali-kali
        }

        Invoice invoice = payment.getInvoice();

        if (invoice == null) {
            throw new IllegalStateException("Payment has no invoice");
        }
        if (providerTxId == null) {
            throw new IllegalStateException("Provider id is null");
        }

        if (invoice.getStatus() == InvoiceStatus.EXPIRED) {
            payment.fail("Invoice expired");
            paymentRepository.save(payment);
            return;
        }

        if (gatewayStatus == PaymentStatus.SUCCESS) {
            payment.complete(providerTxId, providerResponse, LocalDateTime.now());
            invoice.markPaid(LocalDateTime.now());
        } else {
            payment.fail(providerResponse);
            invoice.markFailed(providerResponse);
        }

        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
    }

    public void completePayment(Payment payment, String providerTxId, String providerResponse, LocalDateTime paidAt) {
        if(payment == null) {
            throw new IllegalArgumentException("Payment is null");
        }

        if (providerTxId == null || providerTxId.isBlank()) {
            throw new IllegalArgumentException("Provider transaction id is required");
        }

        if (paidAt == null || paidAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid paidAt");
        }

        if(payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payment can be completed");
        }

        Invoice invoice = payment.getInvoice();

        if(invoice == null) {
            throw new IllegalStateException("Payment has no invoice");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID ||
            invoice.getStatus() == InvoiceStatus.EXPIRED) {
            throw new IllegalStateException("Invoice already finalized");
        }

        if (payment.getAmount() != invoice.getAmount()) {
            throw new IllegalStateException("Payment amount mismatch");
        }

        payment.complete(providerTxId, providerResponse, paidAt);

        invoice.markPaid(paidAt);

        Subscription subscription = invoice.getSubscription();

        if(subscription != null) {
            subscription.onInvoicePaid(invoice);
        }

        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
        subscriptionRepository.save(subscription);
    }

    public void failPayment(Payment payment, String reason, LocalDateTime failedAt) {
        if(payment == null) {
            throw new IllegalArgumentException("Payment is null");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Fail reason is required");
        }

        if (failedAt == null || failedAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid failedAt");
        }

        if (payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }
        
        if(payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payment can be failed");
        }


        Invoice invoice = payment.getInvoice();

        if(invoice == null) {
            throw new IllegalStateException("Payment has no invoice");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice already paid");
        }

        payment.fail(reason);

        invoice.markFailed(reason);

        Subscription subscription = invoice.getSubscription();

        if (subscription != null) {
            subscription.onInvoiceFailed(invoice);
            subscriptionRepository.save(subscription);
        }

        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
    }

    public void retryPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment id not found"));

        if(payment.getStatus() != PaymentStatus.FAILED) {
            throw new IllegalStateException("Only failed payment can be retried");
        }

        Invoice invoice = payment.getInvoice();

        if(invoice == null) {
            throw new IllegalStateException("Invoice is null");
        }

        if (!invoice.isPayable(LocalDateTime.now())) {
            throw new IllegalStateException("Invoice is not payable");
        }

        List<Payment> pendings = paymentRepository.findPendingByInvoiceId(invoice.getId());

        if (!pendings.isEmpty()) {
            throw new IllegalStateException("There is already a pending payment");
        }

        if(payment.getAmount() != invoice.getAmount()) {
            throw new IllegalStateException("Amount is missmatch");
        }

        Payment retry = Payment.attempt(invoice, payment.getAmount(), payment.getMethod());

        paymentRepository.save(retry);

        processPayment(retry.getId());
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment id not found")); 
    }

    public List<Payment> getPaymentsByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }
}