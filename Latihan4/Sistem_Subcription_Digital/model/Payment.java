package Sistem_Subcription_Digital.model;

import java.time.LocalDateTime;

public class Payment {
    public enum PaymentMethod{CARD, VA, EWALLET, MANUAL}
    public enum PaymentStatus{SUCCESS, FAILED, PENDING}

    private Long id;
    private Invoice invoice;
    private double amount;
    private LocalDateTime attemptedAt;
    private PaymentMethod method;
    private PaymentStatus status;
    private String providerResponse;
    private String providerTransactionId;

    public Payment(Long id, Invoice invoice, double amount, LocalDateTime attemptedAt, PaymentMethod method, PaymentStatus status, String providerResponse, String providerTransactionId) {
        this.id = id;
        this.invoice = invoice;
        this.amount = amount;
        this.attemptedAt = attemptedAt;
        this.method = method;
        this.status = status;
        this.providerResponse = providerResponse;
        this.providerTransactionId = providerTransactionId;
    }

    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getProviderResponse() {
        return providerResponse;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public Payment attempt(Invoice invoiceId, double amount, PaymentMethod method) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh kurang dari 1");
        }

        if(invoiceId == null) {
            throw new IllegalArgumentException("Invoice id tidak valid");
        }

        Payment newPayment = new Payment(this.id, invoiceId, amount, LocalDateTime.now(), method, PaymentStatus.SUCCESS, null, null);

        return newPayment;
    }

    public void complete(String providerTransactionId, String providerResponse, LocalDateTime completedAt) {
        if(!this.status.equals(PaymentStatus.PENDING)) {
            throw new IllegalStateException("Status bukan pending");
        }

        this.status = PaymentStatus.SUCCESS;
        this.providerTransactionId = providerTransactionId;
        this.providerResponse = providerResponse;
        this.attemptedAt = completedAt;
    }

    public void fail(String providerResponse) {
        if(!this.status.equals(PaymentStatus.FAILED)) {
            throw new IllegalStateException("Status bukan gagal");
        }

        this.status = PaymentStatus.FAILED;
        this.providerResponse = providerResponse;
        this.attemptedAt = LocalDateTime.now();
    }

    public boolean isSuccessful() {
        return this.status.equals(PaymentStatus.SUCCESS);
    }

    public boolean isFailed() {
        return this.status.equals(PaymentStatus.FAILED);
    }
}