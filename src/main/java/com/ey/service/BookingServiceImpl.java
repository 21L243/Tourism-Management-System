package com.ey.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.BookingRequest;
import com.ey.dto.request.TravellerRequest;
import com.ey.entity.Account;
import com.ey.entity.Booking;
import com.ey.entity.Guide;
import com.ey.entity.TourPackage;
import com.ey.entity.Traveller;
import com.ey.entity.Voucher;
import com.ey.enums.AvailabilityStatus;
import com.ey.enums.BookingStatus;
import com.ey.exception.BadRequestException;
import com.ey.exception.ConflictException;
import com.ey.exception.NotFoundException;
import com.ey.repository.AccountRepository;
import com.ey.repository.BookingRepository;
import com.ey.repository.GuideRepository;
import com.ey.repository.TourPackageRepository;
import com.ey.repository.TravellerRepository;
import com.ey.repository.VoucherRepository;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TourPackageRepository tourPackageRepository;
	@Autowired
	private TravellerRepository travellerRepository;
	@Autowired
	private VoucherRepository voucherRepository;
	@Autowired
	private GuideRepository guideRepository;
	
	Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	@Override
	public Booking create(BookingRequest req) {
		Account acc = accountRepository.findById(req.getAccountId())
				.orElseThrow(() -> new NotFoundException("Account not found"));

		TourPackage tp = tourPackageRepository.findById(req.getTourPackageId())
				.orElseThrow(() -> new NotFoundException("Tour Package not found"));

		LocalDate start = req.getStartDate();
		LocalDate end = req.getEndDate();

		if (end.isBefore(start)) {
			logger.warn("endDate must be after startDate");
			throw new BadRequestException("endDate must be after startDate");
		}
		if (req.getTravellersCount() <= 0) {
			logger.warn("travellersCount must be > 0");
			throw new BadRequestException("travellersCount must be > 0");
		}
		if (req.getTravellersCount() > tp.getCapacity()) {
			logger.warn("Capacity full");
			throw new ConflictException("Capacity full");
		}

		double subtotal = tp.getBasePrice() * req.getTravellersCount();

		Booking booking = new Booking();
		booking.setAccount(acc);
		booking.setTourPackage(tp);
		booking.setStartDate(start);
		booking.setEndDate(end);
		booking.setTravellersCount(req.getTravellersCount());
		booking.setStatus(BookingStatus.PENDING);
		booking.setGuideRequested(req.isGuideRequested());
		booking.setSubtotalAmount(subtotal);
		booking.setDiscountAmount(0.0);
		booking.setTotalAmount(subtotal);

		if (booking.getVouchers() == null) {
			booking.setVouchers(new HashSet<Voucher>());
		}
        logger.info("Booking created successfuly");
		return bookingRepository.save(booking);
	}

	@Override
	public List<Booking> listByAccount(Long accountId) {
		Account acc = accountRepository.findById(accountId)
				.orElseThrow(() -> new NotFoundException("Account not found"));
		logger.info("Accounts list: "+acc);
		return bookingRepository.findByAccount(acc);
	}

	@Override
	public Traveller updateTraveller(Long bookingId, Long travellerId, TravellerRequest req) {
		bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));

		Traveller t = travellerRepository.findById(travellerId)
				.orElseThrow(() -> new NotFoundException("Traveller not found"));

		if (t.getBooking() == null || !t.getBooking().getId().equals(bookingId)) {
			logger.warn("Traveller does not belong to the booking");
			throw new BadRequestException("Traveller does not belong to the booking");
		}

		t.setFullName(req.getFullName());
		t.setAge(req.getAge());
		t.setGender(req.getGender());
		t.setRelationship(req.getRelationship());
		t.setSpecialRequirements(req.getSpecialRequirements());

		Traveller saved = travellerRepository.save(t);
        logger.info("Traveller updated successfully");
		return saved;
	}

	@Override
	public Traveller addTraveller(Long bookingId, TravellerRequest req) {
		Booking b = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));

		int currentCount = b.getTravellersCount();
		int capacity = b.getTourPackage().getCapacity();
		if (currentCount + 1 > capacity) {
			logger.warn("Capacity full");
			throw new ConflictException("Capacity full");
		}

		Traveller t = new Traveller();
		t.setFullName(req.getFullName());
		t.setAge(req.getAge());
		t.setGender(req.getGender());
		t.setRelationship(req.getRelationship());
		t.setSpecialRequirements(req.getSpecialRequirements());

		t.setBooking(b);

		Traveller saved = travellerRepository.save(t);

		b.setTravellersCount(b.getTravellers().size());
		double base = b.getTourPackage().getBasePrice();
		double subtotal = base * b.getTravellersCount();
		b.setSubtotalAmount(subtotal);

		double discount = b.getDiscountAmount();
		if (discount > subtotal) {
			discount = subtotal;
			b.setDiscountAmount(discount);
		}
		b.setTotalAmount(subtotal - discount);

		bookingRepository.save(b);
		logger.info("Traveller added");
		return saved;
	}

	@Override
	public ResponseEntity<?> deleteTraveller(Long bookingId, Long travellerId) {
		Booking b = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));

		Traveller t = travellerRepository.findById(travellerId)
				.orElseThrow(() -> new NotFoundException("Traveller not found"));

		if (t.getBooking() == null || !t.getBooking().getId().equals(bookingId)) {
			logger.warn("Traveller does not belong to the booking");
			throw new BadRequestException("Traveller does not belong to the booking");
		}

		b.getTravellers().remove(t);

		t.setBooking(null);

		int newCount = b.getTravellers().size();
		b.setTravellersCount(newCount);

		double base = b.getTourPackage().getBasePrice();
		double newSubtotal = base * newCount;
		b.setSubtotalAmount(newSubtotal);

		double discount = b.getDiscountAmount();
		if (discount > newSubtotal) {
			discount = newSubtotal;
			b.setDiscountAmount(discount);
		}
		b.setTotalAmount(newSubtotal - discount);

		bookingRepository.save(b);
		logger.info("Deleted " + travellerId + "  Successfully");
		return ResponseEntity.ok("Deleted " + travellerId + " successfully");

	}

	@Override
	public List<Traveller> listTravellers(Long bookingId) {
		Booking b = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
		logger.info("Travellers list: " + b);
		return travellerRepository.findByBooking(b);
	}

	@Override
	public Booking applyVoucher(Long bookingId, String code) {
		Booking b = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));

		Voucher v = voucherRepository.findByCode(code).orElseThrow(() -> new BadRequestException("Voucher invalid"));

		if (!v.isActive()) {
			logger.warn("Voucher inactive");
			throw new BadRequestException("Voucher inactive");
		}

		double discount = 0.0;
		switch (v.getDiscountType()) {
		case PERCENTAGE -> discount = b.getSubtotalAmount() * (v.getDiscountValue() / 100.0);
		case FLAT -> discount = v.getDiscountValue();
		default -> discount = 0.0;
		}

		if (discount > b.getSubtotalAmount()) {
			discount = b.getSubtotalAmount();
		}

		if (b.getVouchers() == null) {
			b.setVouchers(new HashSet<Voucher>());
		}
		b.getVouchers().add(v);

		b.setDiscountAmount(discount);
		b.setTotalAmount(b.getSubtotalAmount() - discount);
		logger.info("Voucher applied");

		return bookingRepository.save(b);
	}

	@Override
	public Booking assignGuide(Long bookingId, Long guideId) {
		Booking b = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
		Guide g = guideRepository.findById(guideId).orElseThrow(() -> new NotFoundException("Guide not found"));

		if (b.getStartDate() == null || b.getEndDate() == null) {
			logger.warn("Booking must have startDate and endDate before assigning a guide");
			throw new BadRequestException("Booking must have startDate and endDate before assigning a guide");
		}

		if (!g.isActive()) {
			logger.warn("Guide is not active");
			throw new BadRequestException("Guide is not active");
		}
		if (g.getAvailabilityStatus() == AvailabilityStatus.BUSY) {
			logger.warn("Guide is busy");
			throw new ConflictException("Guide is busy");
		}
		b.setGuide(g);
		b.setGuideRequested(true);
		logger.info("Guide assigned successfully");
		return bookingRepository.save(b);
	}

}
