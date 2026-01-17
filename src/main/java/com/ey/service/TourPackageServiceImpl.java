package com.ey.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.TourPackageRequest;
import com.ey.entity.Destination;
import com.ey.entity.TourPackage;
import com.ey.enums.AgeEligibility;
import com.ey.exception.BadRequestException;
import com.ey.exception.NotFoundException;
import com.ey.repository.DestinationRepository;
import com.ey.repository.TourPackageRepository;

@Service
public class TourPackageServiceImpl implements TourPackageService {

	@Autowired
	private TourPackageRepository tourPackageRepository;

	@Autowired
	private DestinationRepository destinationRepository;

	Logger logger = LoggerFactory.getLogger(TourPackageServiceImpl.class);

	@Override
	public TourPackage create(TourPackageRequest req) {
		Destination dest = destinationRepository.findById(req.getDestinationId())
				.orElseThrow(() -> new NotFoundException("Destination not found"));

		AgeEligibility eligibility = parseAgeEligibility(req.getAgeEligibility());

		TourPackage tp = new TourPackage();
		tp.setTitle(req.getTitle());
		tp.setDestination(dest);
		tp.setDescription(req.getDescription());
		tp.setDurationDays(req.getDurationDays());
		tp.setBasePrice(req.getBasePrice());
		tp.setCapacity(req.getCapacity());
		tp.setIncludes(req.getIncludes());
		tp.setExcludes(req.getExcludes());
		tp.setAgeEligibility(eligibility);
		tp.setActive(req.isActive());

		logger.info("Tour package created successfully");
		return tourPackageRepository.save(tp);
	}

	@Override
	public TourPackage update(Long id, TourPackageRequest req) {
		TourPackage tp = get(id);

		Destination dest = destinationRepository.findById(req.getDestinationId())
				.orElseThrow(() -> new NotFoundException("Destination not found"));

		AgeEligibility eligibility = parseAgeEligibility(req.getAgeEligibility());

		tp.setTitle(req.getTitle());
		tp.setDestination(dest);
		tp.setDescription(req.getDescription());
		tp.setDurationDays(req.getDurationDays());
		tp.setBasePrice(req.getBasePrice());
		tp.setCapacity(req.getCapacity());
		tp.setIncludes(req.getIncludes());
		tp.setExcludes(req.getExcludes());
		tp.setAgeEligibility(eligibility);
		tp.setActive(req.isActive());

		logger.info("Updated Successfully");
		return tourPackageRepository.save(tp);
	}

	@Override
	public List<TourPackage> list() {
		return tourPackageRepository.findAll();
	}

	@Override
	public TourPackage get(Long id) {
		return tourPackageRepository.findById(id).orElseThrow(() -> new NotFoundException("Tour package not found"));
	}

	@Override
	public List<TourPackage> getByCapacity(int capacity) {
		if (capacity <= 0) {
			logger.warn("Capacity must be greater than 0");
			throw new BadRequestException("Capacity must be greater than 0");
		}
		List<TourPackage> list = tourPackageRepository.findByCapacity(capacity);
		if (list.isEmpty()) {
			logger.warn("Capacity must be greater than 0");
			throw new NotFoundException("No tour packages found with capacity: " + capacity);
		}
		return list;
	}

	@Override
	public List<TourPackage> getByDurationDays(int durationDays) {
		if (durationDays <= 0) {
			logger.warn("Duration must be greater than 0");
			throw new BadRequestException("Duration must be greater than 0");
		}
		List<TourPackage> list = tourPackageRepository.findByDurationDays(durationDays);
		if (list.isEmpty()) {
			logger.warn("No tour packages found with duration days: " + durationDays);
			throw new NotFoundException("No tour packages found with duration days: " + durationDays);
		}
		return list;

	}

	@Override
	public List<TourPackage> getByAgeEligibility(String eligibility) {
		AgeEligibility parsed = parseAgeEligibility(eligibility);
		List<TourPackage> list = tourPackageRepository.findByAgeEligibility(parsed);
		if (list.isEmpty()) {
			logger.warn("No tour packages found with eligibility: " + parsed.name());
			throw new NotFoundException("No tour packages found with eligibility: " + parsed.name());
		}
		logger.info("Tour package list: "+list);
		return list;
	}

	private AgeEligibility parseAgeEligibility(String value) {
		if (value == null || value.isBlank()) {
			logger.warn("Age eligibility is required");
			throw new BadRequestException("Age eligibility is required");
		}
		try {
			return AgeEligibility.valueOf(value.trim().toUpperCase());
		} catch (IllegalArgumentException ex) {
			logger.warn("Invalid age eligibility: " + value);
			throw new BadRequestException("Invalid age eligibility: " + value);
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		return tourPackageRepository.findById(id).map(tp -> {
			tourPackageRepository.deleteById(id);
			return ResponseEntity.ok("Tour package " + id + " deleted successfully");
		}).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour package not found with id: " + id));
	}
}
