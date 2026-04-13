package com.fintech.payment.config;

import com.fintech.payment.provider.PaymentProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProviderConfig {


    @Bean
    public Map<String, PaymentProvider> providerMap(Map<String, PaymentProvider> providers) {
        return providers;
    }
}
