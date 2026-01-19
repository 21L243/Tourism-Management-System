package com.ey.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.TripPlanRequest;
import com.ey.entity.Destination;
import com.ey.entity.TourPackage;
import com.ey.entity.TripPlan;
import com.ey.exception.NotFoundException;
import com.ey.repository.TourPackageRepository;
import com.ey.repository.TripPlanRepository;

@Service
public class TripPlanServiceImpl implements TripPlanService {

	@Autowired
	private TripPlanRepository tripPlanRepository;

	@Autowired
	private TourPackageRepository tourPackageRepository;

	Logger logger = LoggerFactory.getLogger(TripPlanServiceImpl.class);

	@Override
	public TripPlan addToPackage(Long packageId, TripPlanRequest req) {
		TourPackage tp = tourPackageRepository.findById(packageId)
				.orElseThrow(() -> new NotFoundException("Trip package not found"));

		TripPlan plan = new TripPlan();
		plan.setTourPackage(tp);
		plan.setDayNumber(req.getDayNumber());
		plan.setTitle(req.getTitle());
		plan.setDescription(req.getDescription());

		logger.info("Trip plan added successfully");
		return tripPlanRepository.save(plan);
	}

	@Override
	public Map<String, Object> listForPackage(Long packageId) {

		TourPackage tp = tourPackageRepository.findById(packageId)
				.orElseThrow(() -> new NotFoundException("Trip package not found"));

		Map<String, Object> tourPackageBlock = new LinkedHashMap<>();
		tourPackageBlock.put("id", tp.getId());
		tourPackageBlock.put("title", tp.getTitle());
		tourPackageBlock.put("description", tp.getDescription());
		tourPackageBlock.put("durationDays", tp.getDurationDays());
		tourPackageBlock.put("basePrice", tp.getBasePrice());
		tourPackageBlock.put("capacity", tp.getCapacity());
		tourPackageBlock.put("includes", tp.getIncludes());
		tourPackageBlock.put("excludes", tp.getExcludes());
		tourPackageBlock.put("ageEligibility", tp.getAgeEligibility());

		Destination dest = tp.getDestination();
		Map<String, Object> destinationBlock = new LinkedHashMap<>();
		if (dest != null) {
			destinationBlock.put("id", dest.getId());
			destinationBlock.put("name", dest.getName());
			destinationBlock.put("country", dest.getCountry());
			destinationBlock.put("city", dest.getCity());
		}

		List<TripPlan> plans = tripPlanRepository.findByTourPackageOrderByDayNumberAsc(tp);

		List<Map<String, Object>> planItems = plans.stream()
				.sorted(java.util.Comparator.comparingInt(TripPlan::getDayNumber)).map(p -> {
					Map<String, Object> m = new LinkedHashMap<>();
					m.put("id", p.getId());
					m.put("packageId", tp.getId()); 
					m.put("dayNumber", p.getDayNumber());
					m.put("title", p.getTitle());
					m.put("description", p.getDescription());
					return m;
				}).collect(java.util.stream.Collectors.toList());

		
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("tourPackage", tourPackageBlock);
		response.put("destination", destinationBlock);
		response.put("plans", planItems);

		logger.info("List of package with trip plans and destination : "+response);
		return response;
	}

	@Override
	public TripPlan update(Long packageId, Long id, TripPlanRequest req) {

		tourPackageRepository.findById(packageId).orElseThrow(() -> new NotFoundException("Trip package not found"));

		TripPlan plan = tripPlanRepository.findById(id).orElseThrow(() -> new NotFoundException("Trip plan not found"));

		if (plan.getTourPackage() == null || !plan.getTourPackage().getId().equals(packageId)) {
			logger.warn("Trip plan does not belong to the specified package");
			throw new NotFoundException("Trip plan does not belong to the specified package");
		}

		plan.setDayNumber(req.getDayNumber());
		plan.setTitle(req.getTitle());
		plan.setDescription(req.getDescription());

		logger.info("Updated successfully");
		return tripPlanRepository.save(plan);
	}

	@Override
	public ResponseEntity<?> delete(Long packageId, Long id) {

		TripPlan plan = tripPlanRepository.findById(id).orElseThrow(() -> new NotFoundException("Trip plan not found"));

		if (plan.getTourPackage() == null || !plan.getTourPackage().getId().equals(packageId)) {
			logger.warn("Trip plan does not belong to the specified package");
			throw new NotFoundException("Trip plan does not belong to the specified package");
		}

		tripPlanRepository.deleteById(id);
		logger.info("Trip plan " + id + " deleted successfully");
		return ResponseEntity.ok("Trip plan " + id + " deleted successfully");
	}

	@Override
	public TripPlan getById(Long packageId, Long id) {
		TripPlan plan = tripPlanRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Trip plan " + id + " not found"));

		if (plan.getTourPackage() == null || !plan.getTourPackage().getId().equals(packageId)) {

			logger.warn("Trip plan does not belong to the specified package");
			throw new NotFoundException("Trip plan does not belong to the specified package");
		}
		logger.info("Trip plan " + id + " : " + plan);
		return plan;
	}

}
