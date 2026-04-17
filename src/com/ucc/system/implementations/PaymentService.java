package com.ucc.system.implementations;

import com.ucc.system.interfaces.MicroserviceProxy;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements MicroserviceProxy<String> {

    @Override
    public String execute(String operation, Object... params) {
        if (Math.random() < 0.10) {
            throw new RuntimeException("PaymentService failure: payment gateway timeout (simulated).");
        }

        return switch (operation.toLowerCase()) {
            case "process"  -> "Payment processed successfully. Ref: PAY-" + (int)(Math.random() * 90000 + 10000) + ".";
            case "refund"   -> "Refund issued successfully.";
            case "validate" -> "Payment method validated: VISA ending in 4242.";
            case "status"   -> "Payment status: CONFIRMED.";
            default         -> "Payment operation '" + operation + "' completed.";
        };
    }
}