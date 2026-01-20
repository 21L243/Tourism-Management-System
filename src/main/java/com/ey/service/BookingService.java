package com.ey.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ey.dto.request.BookingRequest;
import com.ey.dto.request.TravellerRequest;
import com.ey.entity.Booking;
import com.ey.entity.Traveller;

import jakarta.validation.Valid;

public interface BookingService {
	
	Booking create(BookingRequest req);

	List<Booking> listByAccount(Long accountId);

	Traveller addTraveller(Long bookingId, TravellerRequest req);

	List<Traveller> listTravellers(Long bookingId);

	Booking applyVoucher(Long bookingId, String code);

	ResponseEntity<?> deleteTraveller(Long bookingId, Long travellerId);

	Traveller updateTraveller(Long bookingId, Long travellerId, @Valid TravellerRequest req);

	Booking assignGuide(Long bookingId, Long guideId);


}