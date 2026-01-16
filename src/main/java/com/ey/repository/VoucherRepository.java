package com.ey.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

	
	Optional<Voucher> findByCode(String code);


	boolean existsByCodeIgnoreCase(String normalized);
}