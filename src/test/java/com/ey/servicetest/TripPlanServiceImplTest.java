package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ey.dto.request.TripPlanRequest;
import com.ey.entity.TourPackage;
import com.ey.entity.TripPlan;
import com.ey.exception.NotFoundException;
import com.ey.repository.TourPackageRepository;
import com.ey.repository.TripPlanRepository;
import com.ey.service.TripPlanServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional 
class TripPlanServiceImplTest {

    @Autowired
    private TripPlanServiceImpl tripPlanService;

    @Autowired
    private TripPlanRepository tripPlanRepository;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    private Long packageId;

    @BeforeEach
    void setUp() {
        
        TourPackage tp = new TourPackage();
        tp.setTitle("Test Package");    
        tp.setBasePrice(500.0);         
        tp = tourPackageRepository.save(tp);
        packageId = tp.getId();
    }

    @Test
    void addToPackage_success() {
        TripPlanRequest req = new TripPlanRequest();
        req.setDayNumber(1);
        req.setTitle("Day 1");
        req.setDescription("Arrival");

        TripPlan saved = tripPlanService.addToPackage(packageId, req);

        assertNotNull(saved);
        assertEquals("Day 1", saved.getTitle());
        assertEquals(1, saved.getDayNumber());
        assertEquals("Arrival", saved.getDescription());
        assertNotNull(saved.getTourPackage());
        assertEquals(packageId, saved.getTourPackage().getId());

       
        TripPlan fromDb = tripPlanRepository.findById(saved.getId())
            .orElseThrow(() -> new AssertionError("TripPlan not persisted"));
        assertEquals("Day 1", fromDb.getTitle());
        assertEquals(packageId, fromDb.getTourPackage().getId());
    }

    @Test
    void getById_failure_notFound() {
        Long nonExistingPlanId = 99_999L;
        assertThrows(NotFoundException.class,
            () -> tripPlanService.getById(packageId, nonExistingPlanId));
    }
}
