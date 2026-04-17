package com.ucc.system.web;

import com.ucc.system.implementations.InventoryService;
import com.ucc.system.implementations.OrderService;
import com.ucc.system.implementations.PaymentService;
import com.ucc.system.repository.LogRepository;
import com.ucc.system.wrappers.LoggingHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ServiceController {

    private final InventoryService inventoryService;
    private final OrderService     orderService;
    private final PaymentService   paymentService;
    private final LogRepository    repo;

    public ServiceController(InventoryService inventoryService,
                             OrderService orderService,
                             PaymentService paymentService,
                             LogRepository repo) {
        this.inventoryService = inventoryService;
        this.orderService     = orderService;
        this.paymentService   = paymentService;
        this.repo             = repo;
    }

   
    @PostMapping("/services/inventory/{operation}")
    public ResponseEntity<Map<String, String>> inventory(@PathVariable String operation) {
        LoggingHandler<String> proxy = new LoggingHandler<>(inventoryService, "Inventory", repo);
        try {
            String result = proxy.execute(operation);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ─── Orders ───────────────────────────────────────────────────────────────
    @PostMapping("/services/orders/{operation}")
    public ResponseEntity<Map<String, String>> orders(@PathVariable String operation) {
        LoggingHandler<String> proxy = new LoggingHandler<>(orderService, "Orders", repo);
        try {
            String result = proxy.execute(operation);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

  
    @PostMapping("/services/payments/{operation}")
    public ResponseEntity<Map<String, String>> payments(@PathVariable String operation) {
        LoggingHandler<String> proxy = new LoggingHandler<>(paymentService, "Payments", repo);
        try {
            String result = proxy.execute(operation);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/metrics/simulate-load")
    public ResponseEntity<Map<String, Object>> simulateLoad() {
        Random rnd = new Random();

        List<String> inventoryOps = List.of("check", "reserve", "release", "update");
        List<String> orderOps     = List.of("create", "cancel", "status", "list");
        List<String> paymentOps   = List.of("process", "refund", "validate", "status");

        int success = 0, errors = 0;

        for (int i = 0; i < 50; i++) {
            int svc = rnd.nextInt(3);
            try {
                switch (svc) {
                    case 0 -> {
                        String op = inventoryOps.get(rnd.nextInt(inventoryOps.size()));
                        new LoggingHandler<>(inventoryService, "Inventory", repo).execute(op);
                    }
                    case 1 -> {
                        String op = orderOps.get(rnd.nextInt(orderOps.size()));
                        new LoggingHandler<>(orderService, "Orders", repo).execute(op);
                    }
                    case 2 -> {
                        String op = paymentOps.get(rnd.nextInt(paymentOps.size()));
                        new LoggingHandler<>(paymentService, "Payments", repo).execute(op);
                    }
                }
                success++;
            } catch (Exception e) {
                errors++;
            }
        }

        return ResponseEntity.ok(Map.of(
                "message",     "Simulate-load complete",
                "totalCalls",  50,
                "success",     success,
                "errors",      errors
        ));
    }
}