package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Account;
import com.ey.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"account","tourPackage","travellers","vouchers"})
	List<Booking> findByAccount(Account account);
}