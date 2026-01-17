package com.ey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.dto.request.FeedbackRequest;
import com.ey.entity.Account;
import com.ey.entity.Booking;
import com.ey.entity.Feedback;
import com.ey.exception.NotFoundException;
import com.ey.repository.AccountRepository;
import com.ey.repository.BookingRepository;
import com.ey.repository.FeedbackRepository;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Feedback submit(FeedbackRequest req) {
		Booking b = bookingRepository.findById(req.getBookingId())
				.orElseThrow(() -> new NotFoundException("Booking not found"));

		Account a = accountRepository.findById(req.getAccountId())
				.orElseThrow(() -> new NotFoundException("Account not found"));

		Feedback fb = new Feedback();
		fb.setBooking(b);
		fb.setAccount(a);
		fb.setRating(req.getRating());
		fb.setComments(req.getComments());
		fb.setPublished(false);

		return feedbackRepository.save(fb);
	}

	@Override
	public List<Feedback> list() {
		return feedbackRepository.findAll();
	}
}
