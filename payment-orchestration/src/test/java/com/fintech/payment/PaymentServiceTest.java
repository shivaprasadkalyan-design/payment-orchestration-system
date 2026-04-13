//package com.fintech.payment;
//
//import com.fintech.payment.dto.PaymentRequest;
//import com.fintech.payment.dto.PaymentResponse;
//import com.fintech.payment.enums.Provider;
//import com.fintech.payment.provider.PaymentProvider;
//import com.fintech.payment.repository.PaymentRepository;
//import com.fintech.payment.routing.RoutingEngine;
//import com.fintech.payment.service.IdempotencyService;
//import com.fintech.payment.service.PaymentService;
//import com.fintech.payment.service.impl.PaymentServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentServiceTest {
//
//    @Mock
//    private RoutingEngine routingEngine;
//
//    @Mock
//    private IdempotencyService idempotencyService;
//
//    @Mock
//    private PaymentRepository paymentRepository;
//
//    @Mock
//    private PaymentProvider providerA;
//
//    @Mock
//    private PaymentProvider providerB;
//
//    @InjectMocks
//    private PaymentServiceImpl paymentService;
//
//    private Map<String, PaymentProvider> providerMap;
//
//    @BeforeEach
//    void setup() {
//        providerMap = new HashMap<>();
//        providerMap.put("A", providerA);
//        providerMap.put("B", providerB);
//
//        paymentService = new PaymentServiceImpl(
//                routingEngine,
//                idempotencyService,
//                paymentRepository,
//                providerMap
//        );
//    }
//
//    @Test
//    void shouldReturnExistingResponse_whenIdempotencyKeyExists() {
//
//        String key = "abc";
//        PaymentResponse response = new PaymentResponse("1", "SUCCESS", "A");
//
//        when(idempotencyService.get(key)).thenReturn(Optional.of(response));
//
//        PaymentResponse result = paymentService.createPayment(new PaymentRequest(), key);
//
//        assertEquals("SUCCESS", result.getStatus());
//        verify(paymentRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldRouteToProviderA_forCard() {
//
//        PaymentRequest request = new PaymentRequest();
//        request.setPaymentMethod("CARD");
//
//        when(idempotencyService.get(any())).thenReturn(Optional.empty());
//        when(routingEngine.route("CARD")).thenReturn(Provider.A);
//        when(providerA.process(any())).thenReturn(new PaymentResponse(null, "SUCCESS", "A"));
//
//        PaymentResponse response = paymentService.createPayment(request, "key");
//
//        assertEquals("SUCCESS", response.getStatus());
//    }
//
//    @Test
//    void shouldRetryAndFailover_whenProviderFails() {
//
//        PaymentRequest request = new PaymentRequest();
//        request.setPaymentMethod("UPI");
//
//        when(idempotencyService.get(any())).thenReturn(Optional.empty());
//        when(routingEngine.route("UPI")).thenReturn(Provider.B);
//
//        when(providerB.process(any()))
//                .thenReturn(new PaymentResponse(null, "FAILED", "B"));
//
//        when(providerA.process(any()))
//                .thenReturn(new PaymentResponse(null, "SUCCESS", "A"));
//
//        PaymentResponse response = paymentService.createPayment(request, "key");
//
//        assertEquals("SUCCESS", response.getStatus());
//    }
//
//    @Test
//    void shouldThrowException_whenPaymentNotFound() {
//
//        when(paymentRepository.findById("1")).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class,
//                () -> paymentService.getPayment("1"));
//    }
//}
