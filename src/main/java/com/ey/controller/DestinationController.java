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

import com.ey.dto.request.DestinationRequest;
import com.ey.entity.Destination;
import com.ey.service.DestinationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {
	
	
	@Autowired
	private DestinationService destinationService;

	@PostMapping
	public ResponseEntity<Destination> create(@Valid @RequestBody DestinationRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED).body(destinationService.create(req));
	}

	@GetMapping
	public List<Destination> list() {
		return destinationService.list();
	}

	@GetMapping("/id/{id}")
	public Destination get(@PathVariable Long id) {
		return destinationService.get(id);
	}

	@GetMapping("/name/{name}")
	public Destination getByName(@PathVariable String name) {
		return destinationService.getByName(name);
	}

	@GetMapping("/city/{city}")
	public List<Destination> getByCity(@PathVariable String city) {
		return destinationService.getByCity(city);
	}

	@GetMapping("/country/{country}")
	public List<Destination> getByCountry(@PathVariable String country) {
		return destinationService.getByCountry(country);
	}

	
	@GetMapping("/status/{status}")
	public List<Destination> getByActive(@PathVariable boolean status) {
		return destinationService.getByActive(status);
	}

	@PutMapping("/{id}")
	public Destination update(@PathVariable Long id, @Valid @RequestBody DestinationRequest req) {
		return destinationService.update(id, req);
	}

	@DeleteMapping("/id/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return destinationService.delete(id);
		
	}
	
	@DeleteMapping("/status/{status}")
	public ResponseEntity<?> deleteByActiveStatus(@PathVariable boolean status) {
		return destinationService.deleteByIsActive(status);
		
	}
}