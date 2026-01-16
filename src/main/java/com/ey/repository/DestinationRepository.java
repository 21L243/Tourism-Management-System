package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
	List<Destination> findByNameIgnoreCase(String name);

	List<Destination> findByCountryIgnoreCase(String country);

	List<Destination> findByCityIgnoreCase(String city);

	List<Destination> findByIsActive(boolean active);

	void deleteByIsActive(boolean active);

	long countByIsActive(boolean status);
}
