package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Booking;
import com.ey.entity.Traveller;

public interface TravellerRepository extends JpaRepository<Traveller, Long> {

	@EntityGraph(attributePaths = { "booking" })
	List<Traveller> findByBooking(Booking booking);
}