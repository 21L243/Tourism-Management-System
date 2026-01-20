package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ey.entity.Destination;
import com.ey.entity.TourPackage;
import com.ey.enums.AgeEligibility;
import com.ey.exception.BadRequestException;
import com.ey.repository.DestinationRepository;
import com.ey.repository.TourPackageRepository;
import com.ey.service.TourPackageServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class TourPackageServiceImplTest {

	@Autowired
	private TourPackageServiceImpl tourPackageService;

	@Autowired
	private TourPackageRepository tourPackageRepository;

	@Autowired
	private DestinationRepository destinationRepository;

	private Long existingPackageId;

	@BeforeEach
	void setUp() {

		Destination dest = new Destination();
		dest.setName("Goa");
		dest.setCountry("India");
		dest = destinationRepository.save(dest);

		TourPackage tp = new TourPackage();
		tp.setTitle("Beach Getaway");
		tp.setDestination(dest);
		tp.setDescription("3 days of sun and sand");
		tp.setDurationDays(3);
		tp.setBasePrice(1500.0);
		tp.setCapacity(20);
		tp.setIncludes(List.of("Hotel", "Breakfast"));
		tp.setExcludes(List.of("Flights"));
		tp.setAgeEligibility(AgeEligibility.SENIOR);
		tp.setActive(true);

		tp = tourPackageRepository.save(tp);
		existingPackageId = tp.getId();
	}

	@Test
	void get_success() {
		TourPackage result = tourPackageService.get(existingPackageId);

		assertNotNull(result);
		assertEquals("Beach Getaway", result.getTitle());
		assertEquals(3, result.getDurationDays());
		assertEquals(1500.0, result.getBasePrice());
		assertEquals(20, result.getCapacity());

	}

	@Test
	void getByCapacity_failure_invalidCapacity() {

		assertThrows(BadRequestException.class, () -> tourPackageService.getByCapacity(0));
		assertThrows(BadRequestException.class, () -> tourPackageService.getByCapacity(-1));
	}

}
