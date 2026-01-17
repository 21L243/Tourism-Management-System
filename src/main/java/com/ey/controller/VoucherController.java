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

import com.ey.dto.request.VoucherRequest;
import com.ey.entity.Voucher;
import com.ey.service.VoucherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vouchers")
public class VoucherController {

	@Autowired
	private VoucherService voucherService;

	@PostMapping
	public ResponseEntity<Voucher> create(@Valid @RequestBody VoucherRequest req) {
		Voucher saved = voucherService.create(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping
	public List<Voucher> getAllVouchers() {
		return voucherService.getAll();
	}

	@GetMapping("/{voucherId}")
	public ResponseEntity<Voucher> getVoucherById(@PathVariable Long voucherId) {
		return voucherService.getById(voucherId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{voucherId}")
	public ResponseEntity<Voucher> update(@PathVariable Long voucherId, @Valid @RequestBody VoucherRequest req) {

		Voucher updated = voucherService.update(voucherId, req);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{voucherId}")
	public ResponseEntity<?> deleteVoucher(@PathVariable Long voucherId) {
		return voucherService.delete(voucherId);

	}

}
