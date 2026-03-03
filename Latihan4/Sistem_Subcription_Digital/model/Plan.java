package Sistem_Subcription_Digital.model;

public class Plan {
    public enum Period{MONTHLY, YEARLY};

    private Long id;
    private String code;
    private String name;
    private double pricePeriod;
    private Period period;
    private boolean allowDowngradeMidCycle;

    public Plan(Long id, String code, String name, double pricePeriod, Period period, boolean allowDowngradeMidCycle) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.pricePeriod = pricePeriod;
        this.period = period;
        this.allowDowngradeMidCycle = allowDowngradeMidCycle;
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
    }

    public void setIdForRepository(Long id) { this.id = id; }
}