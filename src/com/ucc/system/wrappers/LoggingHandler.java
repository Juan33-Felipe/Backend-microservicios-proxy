package com.ucc.system.wrappers;

import com.ucc.system.dto.LogRecord;
import com.ucc.system.interfaces.MicroserviceProxy;
import com.ucc.system.repository.LogRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public class LoggingHandler<T> implements MicroserviceProxy<T> {

    private final MicroserviceProxy<T> realService;
    private final String serviceId;
    private final LogRepository repository;

    public LoggingHandler(MicroserviceProxy<T> realService, String serviceId, LogRepository repository) {
        this.realService = realService;
        this.serviceId   = serviceId;
        this.repository  = repository;
    }

    @Override
    public T execute(String operation, Object... params) {
        UUID requestId = UUID.randomUUID();
        long start = System.currentTimeMillis();

        try {
            T result = realService.execute(operation, params);
            long durationMs = System.currentTimeMillis() - start;

            repository.addLog(new LogRecord(
                    serviceId, operation, durationMs,
                    "SUCCESS", requestId, LocalDateTime.now()
            ));

            return result;

        } catch (Exception e) {
            long durationMs = System.currentTimeMillis() - start;
            String errorDetail = e.getMessage();

            repository.addLog(new LogRecord(
                    serviceId, operation, durationMs,
                    "ERROR", requestId, LocalDateTime.now()
            ));

            throw e;
        }
    }
}