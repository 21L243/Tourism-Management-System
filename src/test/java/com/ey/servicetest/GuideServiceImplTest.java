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

import com.ey.dto.request.GuideRequest;
import com.ey.entity.Guide;
import com.ey.exception.NotFoundException;
import com.ey.repository.GuideRepository;
import com.ey.service.GuideServiceImpl;

@SpringBootTest
class GuideServiceImplTest {

	@Autowired
	private GuideServiceImpl guideService;

	@MockBean
	private GuideRepository guideRepository;


	@Test
	void update_success() {

		GuideRequest req = new GuideRequest();
		req.setFullName("Updated Name");

		Guide g = new Guide();
		g.setId(1L);

		when(guideRepository.findById(1L)).thenReturn(Optional.of(g));
		when(guideRepository.save(any(Guide.class))).thenAnswer(i -> i.getArgument(0));

		Guide updated = guideService.update(1L, req);

		assertNotNull(updated);
		assertEquals("Updated Name", updated.getFullName());
	}


	@Test
	void update_failure_notFound() {

		when(guideRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> guideService.update(1L, new GuideRequest()));
	}
}
