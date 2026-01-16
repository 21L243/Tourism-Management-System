package com.ey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.PaymentRequest;
import com.ey.entity.Payment;
import com.ey.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;

	@PostMapping
	public ResponseEntity<Payment> initiate(@Valid @RequestBody PaymentRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(req));
	}

	@GetMapping("/{id}")
	public Payment get(@PathVariable Long id) {
		return paymentService.get(id);
	}
}