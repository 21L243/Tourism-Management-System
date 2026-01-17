package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ey.dto.request.VoucherRequest;
import com.ey.entity.Voucher;
import com.ey.exception.BadRequestException;
import com.ey.repository.VoucherRepository;
import com.ey.service.VoucherServiceImpl;

@SpringBootTest
class VoucherServiceImplTest {

	@Autowired
	private VoucherServiceImpl voucherService;

	@MockBean
	private VoucherRepository voucherRepository;


	@Test
	void create_success() {

		VoucherRequest req = new VoucherRequest();
		req.setCode("ABC");
		req.setDiscountValue(10.0);

		when(voucherRepository.existsByCodeIgnoreCase("ABC")).thenReturn(false);
		when(voucherRepository.save(any())).thenAnswer(i -> i.getArgument(0));

		Voucher v = voucherService.create(req);

		assertNotNull(v);
	}

	
	@Test
	void create_failure_noCode() {

		VoucherRequest req = new VoucherRequest();
		req.setCode(""); 

		assertThrows(BadRequestException.class, () -> voucherService.create(req));
	}
}
