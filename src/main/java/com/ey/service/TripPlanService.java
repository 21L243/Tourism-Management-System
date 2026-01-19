package com.ey.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ey.dto.request.TripPlanRequest;
import com.ey.entity.TripPlan;

public interface TripPlanService {
	
	TripPlan addToPackage(Long packageId, TripPlanRequest req);

	Map<String, Object> listForPackage(Long packageId);

	TripPlan update(Long packageId, Long id, TripPlanRequest req);

	ResponseEntity<?> delete(Long packageId, Long id);

	TripPlan getById(Long packageId, Long id);
}