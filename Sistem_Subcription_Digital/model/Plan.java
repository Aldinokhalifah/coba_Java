package Sistem_Subcription_Digital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Plan {
    public enum Period{MONTHLY, YEARLY};

    private Long id;
    private String code;
    private String name;
    private double pricePeriod;
    private Period period;
    private boolean allowDowngradeMidCycle;
    private boolean isDeprecated;
    private LocalDate deprecationDate;
    private boolean allowNewSubscriptions;
    private boolean isArchived;
    private LocalDate archiveDate;
    private boolean isVisible;
    private List<Double> priceHistory;

    public Plan(Long id, String code, String name, Period period, double pricePeriod) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.pricePeriod = pricePeriod;
        this.period = period;
        this.allowDowngradeMidCycle = allowDowngradeMidCycle;
        this.isDeprecated = false;
        this.deprecationDate = null;
        this.allowNewSubscriptions = true;
        this.isArchived = false;
        this.archiveDate = null;
        this.isVisible = true;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(pricePeriod);
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPricePeriod() {
        return pricePeriod;
    }

    public Period getPeriod() {
        return period;
    }

    public boolean getAllowDowngradeMidCycle() {
        return allowDowngradeMidCycle;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public LocalDate getDeprecationDate() {
        return deprecationDate;
    }

    public boolean isAllowNewSubscriptions() {
        return allowNewSubscriptions;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public LocalDate getArchiveDate() {
        return archiveDate;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public List<Double> getPriceHistory() {
        return new ArrayList<>(priceHistory);
    }

    public void setPeriod(Period period) {
        if(period == null) {
            throw new IllegalArgumentException("Period cannot be null");
        }
        this.period = period;
    }

    public void setName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public void setCode(String code) {
        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code cannot be null or blank");
        }
        this.code = code;
    }

    public void setPricePeriod(double pricePeriod) {
        if(pricePeriod <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.pricePeriod = pricePeriod;
        this.priceHistory.add(pricePeriod);
    }

    public void setDeprecated(boolean deprecated, LocalDate deprecationDate, boolean allowNewSubscriptions) {
        if(deprecated && deprecationDate == null) {
            throw new IllegalArgumentException("Deprecation date cannot be null when deprecating plan");
        }
        if(deprecated && deprecationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deprecation date cannot be in the past");
        }
        this.isDeprecated = deprecated;
        this.deprecationDate = deprecationDate;
        this.allowNewSubscriptions = allowNewSubscriptions;
    }

    public void setArchived(boolean archived) {
        if(archived && isArchived) {
            throw new IllegalStateException("Plan is already archived");
        }
        if(archived && isDeprecated && !LocalDate.now().isAfter(deprecationDate)) {
            throw new IllegalStateException("Cannot archive plan that is still in deprecation period. Wait until deprecation date passes.");
        }
        this.isArchived = archived;
        if(archived) {
            this.archiveDate = LocalDate.now();
        } else {
            this.archiveDate = null;
        }
    }

    public void setVisibility(boolean visible) {
        this.isVisible = visible;
    }

    public void setIdForRepository(Long id) { 
        this.id = id; 
    }
}