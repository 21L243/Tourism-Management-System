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

import com.ey.dto.request.FeedbackRequest;
import com.ey.entity.Account;
import com.ey.entity.Booking;
import com.ey.entity.Feedback;
import com.ey.exception.NotFoundException;
import com.ey.repository.AccountRepository;
import com.ey.repository.BookingRepository;
import com.ey.repository.FeedbackRepository;
import com.ey.service.FeedbackServiceImpl;

@SpringBootTest
class FeedbackServiceImplTest {

	@Autowired
	private FeedbackServiceImpl feedbackService;

	@MockBean
	private FeedbackRepository feedbackRepository;

	@MockBean
	private BookingRepository bookingRepository;

	@MockBean
	private AccountRepository accountRepository;


	@Test
	void submit_success() {

		FeedbackRequest req = new FeedbackRequest();
		req.setBookingId(1L);
		req.setAccountId(2L);
		req.setRating(5);
		req.setComments("Great service!");

		Booking b = new Booking();
		b.setId(1L);

		Account a = new Account();
		a.setId(2L);

		when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
		when(accountRepository.findById(2L)).thenReturn(Optional.of(a));
		when(feedbackRepository.save(any(Feedback.class))).thenAnswer(i -> i.getArgument(0));

		Feedback saved = feedbackService.submit(req);

		assertNotNull(saved);
		assertEquals(5, saved.getRating());
		assertEquals("Great service!", saved.getComments());
	}


	@Test
	void submit_failure_bookingNotFound() {

		FeedbackRequest req = new FeedbackRequest();
		req.setBookingId(1L);
		req.setAccountId(2L);

		when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> feedbackService.submit(req));
	}
}
