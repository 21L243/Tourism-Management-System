package com.ey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ey.dto.request.VoucherRequest;
import com.ey.entity.Voucher;
import com.ey.exception.BadRequestException;
import com.ey.exception.NotFoundException;
import com.ey.repository.VoucherRepository;

@Service
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;

	public Voucher create(VoucherRequest req) {

		String code = req.getCode() == null ? "" : req.getCode().trim();
		if (code.isEmpty()) {
			throw new BadRequestException("Code is required");
		}
		String normalized = code.toUpperCase();
		if (voucherRepository.existsByCodeIgnoreCase(normalized)) {
			throw new BadRequestException("Voucher code already exists: " + normalized);
		}

		if (req.getDiscountValue() == null || req.getDiscountValue() <= 0) {
			throw new BadRequestException("discountValue must be > 0");
		}
		if (req.getValidFrom() != null && req.getValidUntil() != null
				&& req.getValidUntil().isBefore(req.getValidFrom())) {
			throw new BadRequestException("validTo must be after validFrom");
		}

		Voucher v = new Voucher();
		v.setCode(normalized);
		v.setDiscountType(req.getDiscountType());
		v.setDiscountValue(req.getDiscountValue());
		v.setActive(Boolean.TRUE.equals(req.getActive()));
		v.setValidFrom(req.getValidFrom());
		v.setValidUntil(req.getValidUntil());

		return voucherRepository.save(v);
	}

	@Override
	public List<Voucher> getAll() {
		return voucherRepository.findAll();
	}

	@Override
	public Optional<Voucher> getById(Long id) {
		return voucherRepository.findById(id);
	}

	@Override

	public Voucher update(Long voucherId, VoucherRequest req) {
		Voucher v= voucherRepository.findById(voucherId)
				.orElseThrow(() -> new NotFoundException("Voucher not found: " + voucherId));

		
		v.setCode(req.getCode());
		v.setDiscountType(req.getDiscountType());
		v.setDiscountValue(req.getDiscountValue());
		v.setActive(Boolean.TRUE.equals(req.getActive()));
		v.setValidFrom(req.getValidFrom());
		v.setValidUntil(req.getValidUntil());

		return voucherRepository.save(v);
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		if (!voucherRepository.existsById(id)) {
			throw new NotFoundException("Voucher not found");
		}
		voucherRepository.deleteById(id);
		return ResponseEntity.ok("Deleted Successfully");
	}
}
