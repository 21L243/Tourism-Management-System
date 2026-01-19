package com.ey.controller;

import java.util.List;

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

import com.ey.dto.request.TourPackageRequest;
import com.ey.entity.TourPackage;
import com.ey.service.TourPackageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/packages")
public class TourPackageController {

	@Autowired
	private TourPackageService tourPackageService;

	@PostMapping
	public ResponseEntity<TourPackage> create(@Valid @RequestBody TourPackageRequest req) {
		var created = tourPackageService.create(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public ResponseEntity<List<TourPackage>> list() {
		return ResponseEntity.ok(tourPackageService.list());
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<TourPackage> get(@PathVariable Long id) {
		return ResponseEntity.ok(tourPackageService.get(id));
	}

	@GetMapping("/capacity/{capacity}")
	public ResponseEntity<List<TourPackage>> getByCapacity(@PathVariable int capacity) {
		return ResponseEntity.ok(tourPackageService.getByCapacity(capacity));
	}

	@GetMapping("/duration/{duration}")
	public ResponseEntity<List<TourPackage>> getByDurationDays(@PathVariable int duration) {
		return ResponseEntity.ok(tourPackageService.getByDurationDays(duration));
	}

	@GetMapping("/eligibility/{eligibility}")
	public ResponseEntity<List<TourPackage>> getByAgeEligibility(@PathVariable String eligibility) {
		return ResponseEntity.ok(tourPackageService.getByAgeEligibility(eligibility));
	}

	@GetMapping("/dcountry/{dcountry}")
	public ResponseEntity<List<TourPackage>> getByDestinationCountryPath(@PathVariable String dcountry) {
		return ResponseEntity.ok(tourPackageService.getByDestinationCountry(dcountry));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TourPackage> update(@PathVariable Long id, @Valid @RequestBody TourPackageRequest req) {
		var updated = tourPackageService.update(id, req);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return tourPackageService.delete(id);
	}
}
