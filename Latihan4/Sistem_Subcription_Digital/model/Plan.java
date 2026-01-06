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
}