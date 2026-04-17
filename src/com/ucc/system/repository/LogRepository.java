package com.ucc.system.repository;

import com.ucc.system.dto.LogRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LogRepository {

    private final List<LogRecord> logs = new ArrayList<>();

    public void addLog(LogRecord log) {
        logs.add(log);
    }

    public List<LogRecord> getAllLogs() {
        return new ArrayList<>(logs);
    }

    /**
     * Logs filtrados por servicio, estado y rango de fechas, con paginación.
     *
     * @param service  nombre del servicio (null = todos)
     * @param status   SUCCESS | ERROR (null = todos)
     * @param from     fecha inicio (null = sin límite inferior)
     * @param to       fecha fin   (null = sin límite superior)
     * @param page     número de página base 0
     * @param size     tamaño de página
     */
    public List<LogRecord> getFilteredLogs(String service, String status,
                                           LocalDateTime from, LocalDateTime to,
                                           int page, int size) {
        return logs.stream()
                .filter(l -> service == null || service.isBlank()
                        || l.getServiceId().equalsIgnoreCase(service))
                .filter(l -> status == null || status.isBlank()
                        || l.getStatus().equalsIgnoreCase(status))
                .filter(l -> from == null || !l.getTimestamp().isBefore(from))
                .filter(l -> to   == null || !l.getTimestamp().isAfter(to))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public long countFiltered(String service, String status,
                              LocalDateTime from, LocalDateTime to) {
        return logs.stream()
                .filter(l -> service == null || service.isBlank()
                        || l.getServiceId().equalsIgnoreCase(service))
                .filter(l -> status == null || status.isBlank()
                        || l.getStatus().equalsIgnoreCase(status))
                .filter(l -> from == null || !l.getTimestamp().isBefore(from))
                .filter(l -> to   == null || !l.getTimestamp().isAfter(to))
                .count();
    }
}