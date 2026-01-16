package com.ey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.DestinationRequest;
import com.ey.entity.Destination;
import com.ey.exception.NotFoundException;
import com.ey.repository.DestinationRepository;

@Service
public class DestinationServiceImpl implements DestinationService {

	@Autowired
	private DestinationRepository destinationRepository;

	@Override
	public Destination create(DestinationRequest req) {
		Destination d = new Destination();
		d.setName(req.getName());
		d.setCountry(req.getCountry());
		d.setCity(req.getCity());
		d.setDescription(req.getDescription());
		d.setHighlights(req.getHighlights());
		d.setImages(req.getImages());
		d.setActive(req.isActive());
		return destinationRepository.save(d);
	}

	@Override
	public Destination update(Long id, DestinationRequest req) {
		Destination d = get(id);
		d.setName(req.getName());
		d.setCountry(req.getCountry());
		d.setCity(req.getCity());
		d.setDescription(req.getDescription());
		d.setHighlights(req.getHighlights());
		d.setImages(req.getImages());
		d.setActive(req.isActive());
		return destinationRepository.save(d);
	}

	@Override
	public List<Destination> list() {
		return destinationRepository.findAll();
	}

	@Override
	public Destination get(Long id) {
		return destinationRepository.findById(id).orElseThrow(() -> new NotFoundException("Destination not found"));
	}

	public Destination getByName(String name) {
		return destinationRepository.findByNameIgnoreCase(name).stream().findFirst()
				.orElseThrow(() -> new NotFoundException("Destination not found"));
	}

	public List<Destination> getByCity(String city) {

		List<Destination> destinations = destinationRepository.findByCityIgnoreCase(city);
		if (destinations.isEmpty()) {
			throw new NotFoundException("Destination city not found: " + city);
		}
		return destinations;

	}

	public List<Destination> getByCountry(String country) {

		List<Destination> destinations = destinationRepository.findByCityIgnoreCase(country);
		if (destinations.isEmpty()) {
			throw new NotFoundException("Destination country not found: " + country);
		}
		return destinations;

	}

	@Override
	public List<Destination> getByActive(boolean status) {
		List<Destination> results = destinationRepository.findByIsActive(status);
		if (results == null || results.isEmpty()) {

			throw new NotFoundException("No destinations found with active status: " + status);
		}
		return results;
	}

	@Override
	public ResponseEntity<?> delete(Long id) {

		if (destinationRepository.findById(id).isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Destination not found with id: " + id);
		}
		destinationRepository.deleteById(id);
		return ResponseEntity.ok("Destination " + id + " deleted successfully");

	}

	@Override
	public ResponseEntity<?> deleteByIsActive(boolean status) {
		long count = destinationRepository.countByIsActive(status);
		if (count == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No destinations found with active status: " + status);
		}
		destinationRepository.deleteByIsActive(status);
		return ResponseEntity.ok("Deleted " + count + " destination(s) with status " + status);
	}

}
