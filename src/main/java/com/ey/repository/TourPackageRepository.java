package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.TourPackage;
import com.ey.enums.AgeEligibility;

public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {

	@EntityGraph(attributePaths = { "destination" })
	List<TourPackage> findByAgeEligibility(AgeEligibility ageEligibility);

	@EntityGraph(attributePaths = { "destination" })
	List<TourPackage> findByCapacity(int capacity);

	@EntityGraph(attributePaths = { "destination" })
	List<TourPackage> findByDurationDays(int durationDays);

}