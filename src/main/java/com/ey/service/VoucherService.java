package com.ey.service;

import com.ey.dto.request.VoucherCreateRequest;
import com.ey.entity.Voucher;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface VoucherService {

    List<Voucher> getAll();

    Optional<Voucher> getById(Long id);

    ResponseEntity<?> delete(Long id);

	Voucher create(@Valid VoucherCreateRequest req);
}
