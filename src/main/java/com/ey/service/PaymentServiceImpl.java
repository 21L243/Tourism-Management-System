
package com.ey.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.dto.request.PaymentRequest;
import com.ey.entity.Booking;
import com.ey.entity.Payment;
import com.ey.enums.PaymentStatus;
import com.ey.exception.BadRequestException;
import com.ey.exception.NotFoundException;
import com.ey.repository.BookingRepository;
import com.ey.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Override

	public Payment initiate(PaymentRequest req) {

		Booking b = bookingRepository.findById(req.getBookingId())
				.orElseThrow(() -> new NotFoundException("Booking not found"));

		if (Math.abs(b.getTotalAmount() - req.getAmount()) > 0.01) {
			throw new BadRequestException("Amount mismatch");
		}

		Payment p = new Payment();
		p.setBooking(b);
		p.setProvider(req.getProvider());
		p.setTransactionRef("pay_" + UUID.randomUUID());
		p.setAmount(req.getAmount());
		p.setStatus(PaymentStatus.INITIATED);

		p.setStatus(PaymentStatus.SUCCESS);
		p.setPaidAt(Instant.now());

		b.setStatus(com.ey.enums.BookingStatus.CONFIRMED);
		bookingRepository.save(b);

		return paymentRepository.save(p);
	}

	@Override
	public Payment get(Long id) {
		return paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Payment not found"));
	}
}
