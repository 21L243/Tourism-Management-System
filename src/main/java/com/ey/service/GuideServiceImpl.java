package com.ey.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.GuideRequest;
import com.ey.entity.Guide;
import com.ey.exception.NotFoundException;
import com.ey.repository.GuideRepository;

@Service
public class GuideServiceImpl implements GuideService {

	@Autowired
	private  GuideRepository guideRepository;
	
	Logger logger = LoggerFactory.getLogger(GuideServiceImpl.class);

	@Override
	public Guide create(GuideRequest req) {
		Guide g = new Guide();

		g.setFullName(req.getFullName());
		g.setContactPhone(req.getContactPhone());
		g.setYearsOfExperience(req.getYearsOfExperience());
		g.setLanguages(req.getLanguages());
		g.setActive(req.isActive());
		g.setAvailabilityStatus(req.getAvailabilityStatus());
		
		logger.info("Guide created successfully");
		return guideRepository.save(g);
	}

	@Override
	public List<Guide> list() {
		return guideRepository.findAll();
	}

	@Override
	public Guide update(Long id, GuideRequest req) {
		Guide g = guideRepository.findById(id).orElseThrow(() -> new NotFoundException("Guide not found"));
		g.setFullName(req.getFullName());
		g.setContactPhone(req.getContactPhone());
		g.setYearsOfExperience(req.getYearsOfExperience());
		g.setLanguages(req.getLanguages());
		g.setActive(req.isActive());
		g.setAvailabilityStatus(req.getAvailabilityStatus());
		
		logger.info("Guide updated successfully");
		return guideRepository.save(g);
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		if (!guideRepository.existsById(id)) {
			logger.warn("Guide not found");
			throw new NotFoundException("Guide not found");
		}
		guideRepository.deleteById(id);
		logger.info("Deleted guide id "+id+" successfully");
		return ResponseEntity.ok("Deleted guide id "+id+" successfully");
	}
}
