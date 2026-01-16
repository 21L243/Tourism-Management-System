package com.ey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
}