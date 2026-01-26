package Sistem_Subcription_Digital.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import Sistem_Subcription_Digital.model.Invoice;
import Sistem_Subcription_Digital.model.Payment;
import Sistem_Subcription_Digital.model.Payment.PaymentMethod;
import Sistem_Subcription_Digital.model.Payment.PaymentStatus;
import Sistem_Subcription_Digital.repository.InvoiceRepository;
import Sistem_Subcription_Digital.repository.PaymentRepository;
import Sistem_Subcription_Digital.repository.SubscriptionRepository;

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

    public Payment attemptPayment(Invoice invoice, PaymentMethod method) {}

    public Payment processPayment(Long paymentId) {}

    public Payment processPaymentForInvoice(Long invoiceId, PaymentMethod method) {}

    public void handleGatewayCallback(Long paymentId, PaymentStatus gatewayStatus, String providerTxId, String providerResponse) {}

    public void completePayment(Payment payment, String providerTxId, String providerResponse, LocalDateTime paidAt) {}

    public void failPayment(Payment payment, String providerResponse) {}

    public void retryPayment(Long paymentId) {}

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment id not found")); 
    }

    public List<Payment> getPaymentsByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }

    public boolean simulateGatewayCharge(Payment payment) {}
}
