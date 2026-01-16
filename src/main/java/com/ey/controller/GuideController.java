package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.GuideRequest;
import com.ey.entity.Guide;
import com.ey.service.GuideService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/guides")
public class GuideController {
	
	@Autowired
	private GuideService guideService;

	@PostMapping
	public ResponseEntity<Guide> create(@Valid @RequestBody GuideRequest req) {
		return ResponseEntity.status(201).body(guideService.create(req));
	}

	@GetMapping
	public List<Guide> list() {
		return guideService.list();
	}

	@PutMapping("/{id}")
	public Guide update(@PathVariable Long id, @Valid @RequestBody GuideRequest req) {
		return guideService.update(id, req);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return guideService.delete(id);
		
	}
}