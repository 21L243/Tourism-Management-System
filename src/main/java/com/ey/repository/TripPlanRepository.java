package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.TourPackage;
import com.ey.entity.TripPlan;

public interface TripPlanRepository extends JpaRepository<TripPlan, Long> {
	List<TripPlan> findByTourPackageOrderByDayNumberAsc(TourPackage tourPackage);
}