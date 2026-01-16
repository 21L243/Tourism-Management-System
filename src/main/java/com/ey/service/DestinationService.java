package com.ey.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ey.dto.request.DestinationRequest;
import com.ey.entity.Destination;

public interface DestinationService {
	Destination create(DestinationRequest req);

	Destination update(Long id, DestinationRequest req);

	ResponseEntity<?> delete(Long id);

	List<Destination> list();

	Destination get(Long id);

	Destination getByName(String name);

	List<Destination> getByCity(String city);

	List<Destination> getByCountry(String country);

	List<Destination> getByActive(boolean status);

    ResponseEntity<?> deleteByIsActive(boolean status);
}
