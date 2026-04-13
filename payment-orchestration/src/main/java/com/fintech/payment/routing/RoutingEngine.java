package com.fintech.payment.routing;

import com.fintech.payment.enums.Provider;
import org.springframework.stereotype.Component;

@Component
public class RoutingEngine {

    public Provider route(String method) {
        return switch (method.toUpperCase()) {
            case "CARD" -> Provider.A;
            case "UPI" -> Provider.B;
            default -> throw new RuntimeException("Unsupported method");
        };
    }
}
