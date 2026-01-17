package com.ey.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.TripPlanRequest;
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
	public List<TripPlan> listForPackage(Long packageId) {
		TourPackage tp = tourPackageRepository.findById(packageId)
				.orElseThrow(() -> new NotFoundException("Trip package not found"));
		logger.info("Trip plan list: " + tp);
		return tripPlanRepository.findByTourPackageOrderByDayNumberAsc(tp);
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
		logger.info("Trip plan "+id+" : "+plan);
		return plan;
	}

}
