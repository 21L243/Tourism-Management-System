package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ey.entity.TourPackage;
import com.ey.exception.BadRequestException;
import com.ey.repository.DestinationRepository;
import com.ey.repository.TourPackageRepository;
import com.ey.service.TourPackageServiceImpl;

@SpringBootTest
class TourPackageServiceImplTest {

	@Autowired
	private TourPackageServiceImpl tourPackageService;

	@MockBean
	private TourPackageRepository tourPackageRepository;

	@MockBean
	private DestinationRepository destinationRepository;


	@Test
	void get_success() {
		TourPackage tp = new TourPackage();
		tp.setId(1L);
		tp.setTitle("Beach Getaway");

		when(tourPackageRepository.findById(1L)).thenReturn(Optional.of(tp));

		TourPackage result = tourPackageService.get(1L);

		assertNotNull(result);
		assertEquals("Beach Getaway", result.getTitle());
	}

	@Test
	void getByCapacity_failure_invalidCapacity() {
		assertThrows(BadRequestException.class, () -> tourPackageService.getByCapacity(0));
	}
}
