package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ey.dto.request.PaymentRequest;
import com.ey.entity.Booking;
import com.ey.entity.Payment;
import com.ey.enums.PaymentStatus;
import com.ey.exception.BadRequestException;
import com.ey.repository.BookingRepository;
import com.ey.repository.PaymentRepository;
import com.ey.service.PaymentServiceImpl;

@SpringBootTest
class PaymentServiceImplTest {

	@Autowired
	private PaymentServiceImpl paymentService;

	@MockBean
	private PaymentRepository paymentRepository;

	@MockBean
	private BookingRepository bookingRepository;

	@Test
	void initiate_success() {

		PaymentRequest req = new PaymentRequest();
		req.setBookingId(1L);
		req.setAmount(5000.0);
		req.setProvider("UPI");

		Booking b = new Booking();
		b.setId(1L);
		b.setTotalAmount(5000.0);

		when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
		when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

		Payment p = paymentService.initiate(req);

		assertNotNull(p);
		assertEquals(5000.0, p.getAmount());
		assertEquals(PaymentStatus.SUCCESS, p.getStatus());
	}

	@Test
	void initiate_failure_amountMismatch() {

		PaymentRequest req = new PaymentRequest();
		req.setBookingId(1L);
		req.setAmount(4000.0);

		Booking b = new Booking();
		b.setId(1L);
		b.setTotalAmount(5000.0);

		when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));

		assertThrows(BadRequestException.class, () -> paymentService.initiate(req));
	}
}
