package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ey.dto.request.VoucherRequest;
import com.ey.entity.Voucher;
import com.ey.exception.BadRequestException;
import com.ey.repository.VoucherRepository;
import com.ey.service.VoucherServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class VoucherServiceImplTest {

	@Autowired
	private VoucherServiceImpl voucherService;

	@Autowired
	private VoucherRepository voucherRepository;

	@Test
	void create_success() {

		String uniqueCode = ("ABC-" + UUID.randomUUID()).toUpperCase();

		VoucherRequest req = new VoucherRequest();
		req.setCode(uniqueCode);
		req.setDiscountValue(10.0);

		Voucher v = voucherService.create(req);

		assertNotNull(v);
		assertEquals(uniqueCode, v.getCode());
		assertEquals(10.0, v.getDiscountValue());

		Voucher fromDb = voucherRepository.findByCode(uniqueCode)
				.orElseThrow(() -> new AssertionError("Voucher not found after create"));
		assertEquals(uniqueCode, fromDb.getCode());
		assertEquals(10.0, fromDb.getDiscountValue());
	}

	@Test
	void create_failure_noCode() {
		VoucherRequest req = new VoucherRequest();
		req.setCode(""); 
		req.setDiscountValue(5.0);

		assertThrows(BadRequestException.class, () -> voucherService.create(req));
	}

	
}
