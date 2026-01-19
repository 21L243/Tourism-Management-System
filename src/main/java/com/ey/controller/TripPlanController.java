package com.ey.controller;

import java.util.Map;

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

import com.ey.dto.request.TripPlanRequest;
import com.ey.entity.TripPlan;
import com.ey.service.TripPlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/packages/{packageId}/trip-plans")
public class TripPlanController {
	@Autowired
	private TripPlanService tripPlanService;

	@PostMapping
	public ResponseEntity<TripPlan> add(@PathVariable Long packageId, @Valid @RequestBody TripPlanRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(tripPlanService.addToPackage(packageId, req));
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getPlanswithPackageandDestination(@PathVariable Long packageId) {
		return ResponseEntity.ok(tripPlanService.listForPackage(packageId)); 
	}

	@GetMapping("/{id}")
	public ResponseEntity<TripPlan> getById(@PathVariable("packageId") Long packageId, @PathVariable("id") Long id) {
		var plan = tripPlanService.getById(packageId, id);
		return ResponseEntity.ok(plan);
	}

	@PutMapping("/{id}")
	public TripPlan update(@PathVariable Long packageId, @PathVariable Long id,
			@Valid @RequestBody TripPlanRequest req) {
		return tripPlanService.update(packageId, id, req);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("packageId") Long packageId, @PathVariable("id") Long id) {
		return tripPlanService.delete(packageId, id);
	}
}