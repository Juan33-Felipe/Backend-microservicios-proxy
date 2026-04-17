package com.ucc.system.web;

import com.ucc.system.dto.LogRecord;
import com.ucc.system.dto.StatsSummary;
import com.ucc.system.repository.LogRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final LogRepository repository;

    public MetricsController(LogRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/summary")
    public List<StatsSummary> getSummary() {
        List<LogRecord> all = repository.getAllLogs();

        Map<String, List<LogRecord>> byService = all.stream()
                .collect(Collectors.groupingBy(LogRecord::getServiceId));

        List<StatsSummary> summaries = new ArrayList<>();
        for (Map.Entry<String, List<LogRecord>> entry : byService.entrySet()) {
            String serviceName       = entry.getKey();
            List<LogRecord> records  = entry.getValue();
            long totalCalls          = records.size();
            long errorCount          = records.stream()
                    .filter(r -> "ERROR".equals(r.getStatus())).count();
            double avgDuration       = records.stream()
                    .mapToLong(LogRecord::getDurationMs)
                    .average()
                    .orElse(0);
            summaries.add(new StatsSummary(serviceName, totalCalls, errorCount, avgDuration));
        }

        return summaries;
    }

    @GetMapping("/logs")
    public Map<String, Object> getLogs(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<LogRecord> data  = repository.getFilteredLogs(service, status, from, to, page, size);
        long total            = repository.countFiltered(service, status, from, to);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page",       page);
        response.put("size",       size);
        response.put("totalItems", total);
        response.put("totalPages", (long) Math.ceil((double) total / size));
        response.put("data",       data);
        return response;
    }
}