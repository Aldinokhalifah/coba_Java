package Sistem_Subcription_Digital.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Subscription {
    public enum SubscriptionStatus{ACTIVE, SUSPENDED, EXPIRED, CANCELLED};

    private Long id;
    private User user;
    private Plan plan;
    private LocalDateTime createdAt, startDate, endDate;
    private SubscriptionStatus status;
    private boolean autoRenew;

    public Subscription(Long id, User user, Plan plan, LocalDateTime createdAt, LocalDateTime startDate, LocalDateTime endDate, SubscriptionStatus status, boolean autoRenew) {
        this.id = id;
        this.user = user;
        this.plan = plan;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.autoRenew = autoRenew;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Plan getPlan() {
        return plan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public boolean getAutoRenew() {
        return autoRenew;
    }

    private static LocalDateTime calculateEndDate(LocalDateTime startDate, Plan.Period cycle) {
        if(cycle == Plan.Period.MONTHLY) {
            return startDate.plusMonths(1);
        } else if(cycle == Plan.Period.YEARLY) {
            return startDate.plusYears(1);
        }
        return startDate;
    }

    public void setIdForRepository(Long id) { this.id = id; }

    public static Subscription create(User user, Plan plan, LocalDateTime startDate, Plan.Period cycle) {
        if(user == null) {
            throw new IllegalArgumentException("User tidak valid");
        }
        if(plan == null) {
            throw new IllegalArgumentException("Plan tidak valid");
        }
        if(startDate == null) {
            throw new IllegalArgumentException("Tanggal mulai tidak valid");
        }
        if(cycle == null) {
            throw new IllegalArgumentException("Cycle tidak valid");
        }
        
        Subscription subscription = new Subscription(
            null,
            user,
            plan,
            LocalDateTime.now(),
            startDate,
            calculateEndDate(startDate, cycle),
            SubscriptionStatus.ACTIVE,
            true
        );
        
        return subscription;
    }

    public void upgradeTo(Plan newPlan, LocalDate effectiveDate) {
        if(newPlan == null) {
            throw new IllegalArgumentException("Plan baru tidak valid");
        }
        
        if(effectiveDate == null) {
            throw new IllegalArgumentException("Tanggal efektif tidak valid");
        }
        
        if(this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalArgumentException("Hanya subscription aktif yang dapat di-upgrade");
        }
        
        if(newPlan.getId().equals(this.plan.getId())) {
            throw new IllegalArgumentException("Plan baru harus berbeda dengan plan saat ini");
        }
        
        this.plan = newPlan;
        this.startDate = effectiveDate.atStartOfDay();
        this.endDate = calculateEndDate(this.startDate, newPlan.getPeriod());
    }

    public void downgradeTo(Plan newPlan, LocalDate effectiveDate) {
        if(newPlan == null) {
            throw new IllegalArgumentException("Plan baru tidak valid");
        }
        
        if(effectiveDate == null) {
            throw new IllegalArgumentException("Tanggal efektif tidak valid");
        }
        
        if(this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalArgumentException("Hanya subscription aktif yang dapat di-downgrade");
        }

        if(newPlan.getId().equals(this.plan.getId())) {
            throw new IllegalArgumentException("Plan baru harus berbeda dengan plan saat ini");
        }
        
        if(newPlan.getPricePeriod() >= this.plan.getPricePeriod()) {
            throw new IllegalArgumentException("Plan baru harus memiliki harga lebih rendah");
        }
        
        if(!effectiveDate.equals(this.endDate.toLocalDate())) {
            throw new IllegalArgumentException("Tanggal efektif harus sama dengan tanggal berakhir subscription");
        }
        
        this.plan = newPlan;
        this.startDate = effectiveDate.atStartOfDay();
        this.endDate = calculateEndDate(this.startDate, newPlan.getPeriod());
    }

    public void suspend() {
        if(!this.status.equals(SubscriptionStatus.ACTIVE)) {
            throw new IllegalStateException("Status tidak aktif");
        }

        this.status = SubscriptionStatus.SUSPENDED;
    }

    public void resume() {
        if(!this.status.equals(SubscriptionStatus.SUSPENDED)) {
            throw new IllegalStateException("Hanya subscription yang SUSPENDED bisa resume");
        }

        this.status = SubscriptionStatus.ACTIVE;
    }

    public void expire() {
        if(!(this.status.equals(SubscriptionStatus.ACTIVE) || this.status.equals(SubscriptionStatus.SUSPENDED))) {
            throw new IllegalStateException("Status aktif");
        }

        this.status = SubscriptionStatus.EXPIRED;
    }

    public void cancel() {
        if(this.status.equals(SubscriptionStatus.CANCELLED)) {
            throw new IllegalStateException("Status sudah dicancel");
        }

        this.status = SubscriptionStatus.CANCELLED;
        this.endDate = LocalDateTime.now();
    }

    public void onInvoicePaid(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice tidak valid");
        }

        if (!this.equals(invoice.getSubscription())) {
            throw new IllegalStateException("Invoice bukan milik subscription ini");
        }

        if (this.status == SubscriptionStatus.CANCELLED ||
            this.status == SubscriptionStatus.EXPIRED) {
            throw new IllegalStateException("Subscription sudah berakhir");
        }

        // kasus pertama kali subscribe
        if (this.status == SubscriptionStatus.SUSPENDED) {
            this.status = SubscriptionStatus.ACTIVE;
        }

        // perpanjang masa aktif
        this.startDate = LocalDateTime.now();
        this.endDate = calculateEndDate(this.startDate, this.plan.getPeriod());
    }

    public void onInvoiceFailed(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice tidak valid");
        }

        if (!this.equals(invoice.getSubscription())) {
            throw new IllegalStateException("Invoice bukan milik subscription ini");
        }

        // jangan suspend kalau sudah cancel / expired
        if (this.status == SubscriptionStatus.ACTIVE) {
            this.status = SubscriptionStatus.SUSPENDED;
        }
    }
}   
