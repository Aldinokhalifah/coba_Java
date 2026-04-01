package Sistem_Subcription_Digital.model;

import java.time.LocalDateTime;

public class Invoice {
    public enum InvoiceStatus{PENDING, PAID, FAILED, EXPIRED}

    private Long id;
    private Subscription subscription;
    private LocalDateTime invoiceDate, dueDate;
    private double amount;
    private InvoiceStatus status;
    private LocalDateTime paidAt;
    private int attemptCount;
    private String failureReason;

    public Invoice(Long id, Subscription subscription, LocalDateTime invoiceDate, LocalDateTime dueDate, double amount, InvoiceStatus status, LocalDateTime paidAt, int attemptCount, String failureReason) {
        this.id = id;
        this.subscription = subscription;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
        this.attemptCount = attemptCount;
        this.failureReason = failureReason;
    }

    public Long getId() {
        return id;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public double getAmount() {
        return amount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setIdForRepository(Long id) { this.id = id; }

    public void markPaid(LocalDateTime paidAt) {
        if(this.status.equals(InvoiceStatus.PAID) || this.status.equals(InvoiceStatus.EXPIRED)) {
            throw new IllegalStateException("Invoice sudah dibayar atau kadaluarsa");
        }

        if(this.amount <= 0) {
            throw new IllegalStateException("Jumlah tidak valid");
        }

        if(this.subscription == null) {
            throw new IllegalStateException("Subcription tidak valid");
        }

        if(this.invoiceDate == null) {
            throw new IllegalStateException("Tanggal invoice tidak valid");
        }

        if(this.dueDate == null || this.dueDate.isBefore(invoiceDate)) {
            throw new IllegalStateException("Tanggal berakhir invoice tidak valid");
        }

        this.status = InvoiceStatus.PAID;
        this.paidAt = paidAt;
        this.failureReason = null;
    }

    public void markFailed(String reason) {
        if(!this.status.equals(InvoiceStatus.PENDING)) {
            throw new IllegalStateException("Pembayaran kadaluarsa atau bukan pending");
        }

        if(this.subscription == null) {
            throw new IllegalStateException("Subcription tidak valid");
        }

        if(this.invoiceDate == null) {
            throw new IllegalStateException("Tanggal invoice tidak valid");
        }

        this.status = InvoiceStatus.FAILED;
        attemptCount++;
        this.failureReason = reason;
    }

    public void expire() {
        if(this.status.equals(InvoiceStatus.PAID)) {
            throw new IllegalStateException("Pembayaran sudah dilakukan");
        }

        if(this.invoiceDate == null) {
            throw new IllegalStateException("Tanggal invoice tidak valid");
        }

        if(this.dueDate == null || this.dueDate.isBefore(invoiceDate)) {
            throw new IllegalStateException("Tanggal berakhir invoice tidak valid");
        }

        this.status = InvoiceStatus.EXPIRED;
    }

    public boolean isPayable(LocalDateTime now) {
        if(this.invoiceDate == null) {
            throw new IllegalStateException("Tanggal invoice tidak valid");
        }

        if(this.dueDate == null || this.dueDate.isBefore(invoiceDate)) {
            throw new IllegalStateException("Tanggal berakhir invoice tidak valid");
        }

        return (this.status.equals(InvoiceStatus.PENDING) || this.status.equals(InvoiceStatus.FAILED)) && this.dueDate.isAfter(now);
    }

    public void incrementAttempt() {
        attemptCount++;
    }
}
