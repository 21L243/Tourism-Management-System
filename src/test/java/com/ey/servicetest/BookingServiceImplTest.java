package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import com.ey.entity.Booking;
import com.ey.entity.Guide;
import com.ey.entity.TourPackage;
import com.ey.entity.Traveller;
import com.ey.enums.AvailabilityStatus;
import com.ey.exception.ConflictException;
import com.ey.repository.BookingRepository;
import com.ey.repository.GuideRepository;
import com.ey.repository.TravellerRepository;
import com.ey.service.BookingServiceImpl;

@SpringBootTest
class BookingServiceImplTest {

	@Autowired
	private BookingServiceImpl bookingService;

	@MockBean
	private BookingRepository bookingRepository;
	@MockBean
	private TravellerRepository travellerRepository;
	@MockBean
	private GuideRepository guideRepository;
	

	@Test
	void deleteTraveller_success() {
		Booking b = new Booking();
		b.setId(1L);
		TourPackage tp = new TourPackage();
		tp.setBasePrice(500.0);
		b.setTourPackage(tp);

		Traveller t = new Traveller();
		t.setId(10L);
		t.setBooking(b);

		Set<Traveller> set = new HashSet<>();

		set.add(t);
		b.setTravellers(set);
		b.setTravellersCount(1);

		when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
		when(travellerRepository.findById(10L)).thenReturn(Optional.of(t));
		when(bookingRepository.save(any())).thenReturn(b);

		ResponseEntity<?> res = bookingService.deleteTraveller(1L, 10L);

		assertEquals(200, res.getStatusCodeValue());

	}

	@Test
	void assignGuide_failure_busy() {
		Booking b = new Booking();
		b.setId(1L);
		b.setStartDate(LocalDate.now());
		b.setEndDate(LocalDate.now().plusDays(2));

		Guide g = new Guide();
		g.setActive(true);
		g.setAvailabilityStatus(AvailabilityStatus.BUSY);

		when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));
		when(guideRepository.findById(1L)).thenReturn(Optional.of(g));

		assertThrows(ConflictException.class, () -> bookingService.assignGuide(1L, 1L));
	}

}
