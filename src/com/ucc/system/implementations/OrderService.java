package com.ucc.system.implementations;

import com.ucc.system.interfaces.MicroserviceProxy;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements MicroserviceProxy<String> {

    @Override
    public String execute(String operation, Object... params) {
        return switch (operation.toLowerCase()) {
            case "create"   -> "Order created with ID #ORD-" + (int)(Math.random() * 90000 + 10000) + ".";
            case "cancel"   -> "Order cancelled successfully.";
            case "status"   -> "Order status: IN_TRANSIT.";
            case "list"     -> "Orders retrieved: 5 active orders found.";
            default         -> "Order service: operation '" + operation + "' executed successfully.";
        };
    }
}