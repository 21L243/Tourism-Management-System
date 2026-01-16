package com.ey.service;

import com.ey.dto.request.PaymentRequest;
import com.ey.entity.Payment;

public interface PaymentService {
	Payment initiate(PaymentRequest req);

	Payment get(Long id);
}
