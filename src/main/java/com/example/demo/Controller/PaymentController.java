package com.example.demo.Controller;

import com.example.demo.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/createOrder")
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, Object> request) {
        try {
            double amount = (Double) request.get("amount");
            String receiptId = (String) request.get("receiptId");
            String paymentLink = paymentService.createPaymentLink(amount, receiptId);

            Map<String, String> response = new HashMap<>();
            response.put("paymentLink", paymentLink);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
