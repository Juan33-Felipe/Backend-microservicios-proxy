package com.ucc.system.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class LogRecord {
    private String serviceId;
    private String operation;
    private long durationMs;
    private String status;
    private UUID requestId;
    private LocalDateTime timestamp;

    public LogRecord(String serviceId, String operation, long durationMs,
                     String status, UUID requestId, LocalDateTime timestamp) {
        this.serviceId = serviceId;
        this.operation = operation;
        this.durationMs = durationMs;
        this.status = status;
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    public String getServiceId()       { return serviceId; }
    public String getOperation()       { return operation; }
    public long getDurationMs()        { return durationMs; }
    public String getStatus()          { return status; }
    public UUID getRequestId()         { return requestId; }
    public LocalDateTime getTimestamp(){ return timestamp; }
}