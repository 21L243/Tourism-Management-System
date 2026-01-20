package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.BookingRequest;
import com.ey.dto.request.TravellerRequest;
import com.ey.dto.request.VoucherApplyRequest;
import com.ey.entity.Booking;
import com.ey.entity.Traveller;
import com.ey.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping
	public ResponseEntity<Booking> create(@Valid @RequestBody BookingRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(req));
	}

	@GetMapping("/account/{accountId}")
	public List<Booking> listByAccount(@PathVariable Long accountId) {
		return bookingService.listByAccount(accountId);
	}

	@PutMapping("/{bookingId}/travellers/{travellerId}")
	public ResponseEntity<Traveller> updateTraveller(@PathVariable Long bookingId, @PathVariable Long travellerId,
			@Valid @RequestBody TravellerRequest req) {

		Traveller updated = bookingService.updateTraveller(bookingId, travellerId, req);
		return ResponseEntity.ok(updated);
	}

	@PostMapping("/{id}/travellers")
	public ResponseEntity<Traveller> addTraveller(@PathVariable Long id, @Valid @RequestBody TravellerRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.addTraveller(id, req));
	}

	@GetMapping("/{id}/travellers")
	public List<Traveller> listTravellers(@PathVariable Long id) {
		return bookingService.listTravellers(id);
	}

	@PostMapping("/{id}/apply-voucher")
	public ResponseEntity<?> applyVoucher(@PathVariable Long id, @Valid @RequestBody VoucherApplyRequest req) {

		bookingService.applyVoucher(id, req.getCode());
		return ResponseEntity.ok("Applied voucher");
	}

	
	@PutMapping("/{bookingId}/guide/{guideId}")
	public ResponseEntity<?> assignGuide(@PathVariable Long bookingId, @PathVariable Long guideId) {
		bookingService.assignGuide(bookingId, guideId);
		return ResponseEntity.ok("Guide Added");
	}

	@DeleteMapping("/{bookingId}/travellers/{travellerId}")
	public ResponseEntity<?> deleteTraveller(@PathVariable Long bookingId, @PathVariable Long travellerId) {
		return bookingService.deleteTraveller(bookingId, travellerId);

	}

}