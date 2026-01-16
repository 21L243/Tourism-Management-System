package com.ey.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ey.dto.request.TourPackageRequest;
import com.ey.entity.TourPackage;

public interface TourPackageService {
	
	TourPackage create(TourPackageRequest req);

	TourPackage update(Long id, TourPackageRequest req);

	ResponseEntity<?> delete(Long id);

	List<TourPackage> list();

	TourPackage get(Long id);

	List<TourPackage> getByCapacity(int capacity);

	List<TourPackage> getByDurationDays(int duration);

	List<TourPackage> getByAgeEligibility(String eligibility);
}