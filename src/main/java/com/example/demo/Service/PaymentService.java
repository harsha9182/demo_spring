package com.example.demo.Service;

import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String keyId;

    @Value("${razorpay.secret}")
    private String keySecret;

    public String createPaymentLink(double amount, String receiptId) throws Exception {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", receiptId);
        options.put("description", "Test Transaction");
        options.put("customer", new JSONObject()
                .put("name", "Your Name")
                .put("email", "your_email@example.com")
                .put("contact", "9999999999"));
        options.put("notify", new JSONObject()
                .put("sms", true)
                .put("email", true));
        options.put("reminder_enable", true);
        options.put("notes", new JSONObject()
                .put("address", "Razorpay Corporate Office"));
        options.put("callback_url", "https://example.com/payment/success");
        options.put("callback_method", "get");

        JSONObject paymentLink = razorpayClient.paymentLink.create(options).toJson();
        return paymentLink.getString("short_url");
    }
}
