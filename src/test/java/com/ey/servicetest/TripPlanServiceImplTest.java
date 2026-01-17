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

import com.ey.dto.request.TripPlanRequest;
import com.ey.entity.TourPackage;
import com.ey.entity.TripPlan;
import com.ey.exception.NotFoundException;
import com.ey.repository.TourPackageRepository;
import com.ey.repository.TripPlanRepository;
import com.ey.service.TripPlanServiceImpl;

@SpringBootTest
class TripPlanServiceImplTest {

	@Autowired
	private TripPlanServiceImpl tripPlanService;

	@MockBean
	private TripPlanRepository tripPlanRepository;

	@MockBean
	private TourPackageRepository tourPackageRepository;

	@Test
	void addToPackage_success() {
		Long packageId = 1L;

		TourPackage tp = new TourPackage();
		tp.setId(packageId);

		TripPlanRequest req = new TripPlanRequest();
		req.setDayNumber(1);
		req.setTitle("Day 1");
		req.setDescription("Arrival");

		when(tourPackageRepository.findById(packageId)).thenReturn(Optional.of(tp));
		when(tripPlanRepository.save(any(TripPlan.class))).thenAnswer(i -> i.getArgument(0));

		TripPlan saved = tripPlanService.addToPackage(packageId, req);

		assertNotNull(saved);
		assertEquals("Day 1", saved.getTitle());
		assertEquals(tp, saved.getTourPackage());
	}

	
	@Test
	void getById_failure_notFound() {
		Long packageId = 1L;
		Long planId = 99L;

		when(tripPlanRepository.findById(planId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> tripPlanService.getById(packageId, planId));
	}
}
