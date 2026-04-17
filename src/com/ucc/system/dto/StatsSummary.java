package com.ucc.system.dto;

public class StatsSummary {
    private String serviceName;
    private long totalCalls;
    private long errorCount;
    private double errorRate;
    private double averageDurationMs;

    public StatsSummary(String serviceName, long totalCalls, long errorCount, double averageDurationMs) {
        this.serviceName = serviceName;
        this.totalCalls = totalCalls;
        this.errorCount = errorCount;
        this.errorRate = totalCalls > 0 ? (double) errorCount / totalCalls * 100 : 0;
        this.averageDurationMs = averageDurationMs;
    }

    public String getServiceName()      { return serviceName; }
    public long getTotalCalls()         { return totalCalls; }
    public long getErrorCount()         { return errorCount; }
    public double getErrorRate()        { return errorRate; }
    public double getAverageDurationMs(){ return averageDurationMs; }
}