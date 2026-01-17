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

import com.ey.entity.Destination;
import com.ey.exception.NotFoundException;
import com.ey.repository.DestinationRepository;
import com.ey.service.DestinationServiceImpl;

@SpringBootTest
class DestinationServiceImplTest {

	@Autowired
	private DestinationServiceImpl destinationService;

	@MockBean
	private DestinationRepository destinationRepository;

	@Test
	void get_success() {
		Destination d = new Destination();
		d.setId(1L);
		d.setName("Paris");

		when(destinationRepository.findById(1L)).thenReturn(Optional.of(d));

		Destination result = destinationService.get(1L);

		assertNotNull(result);
		assertEquals("Paris", result.getName());
	}

	@Test
	void get_failure_notFound() {
		when(destinationRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> destinationService.get(1L));
	}

}
