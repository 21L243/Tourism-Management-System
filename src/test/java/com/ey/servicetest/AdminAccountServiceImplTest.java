package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ey.dto.response.AccountDetailsResponse;
import com.ey.entity.Account;
import com.ey.enums.Role;
import com.ey.exception.NotFoundException;
import com.ey.repository.AccountRepository;
import com.ey.service.AdminAccountServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class AdminAccountServiceImplTest {

	@Autowired
	private AdminAccountServiceImpl adminAccountServiceImpl;

	@Autowired
	private AccountRepository accountRepository;

	private Long existingId;

	@BeforeEach
	void setUp() {

		Account acc = new Account();
		acc.setFullName("Test User");
		acc.setEmail("dummy@gmail.com");
		acc.setPasswordHash("dummyHash");
		acc.setPhone("9999999999");
		acc.setRole(Role.ADMIN);
		acc.setActive(true);

		existingId = accountRepository.save(acc).getId();
	}

	@Test
	void getAccountById_success() {
		AccountDetailsResponse response = adminAccountServiceImpl.getAccountById(existingId);
		assertNotNull(response);

	}

	@Test
	void getAccountById_failure() {
		Long missingId = existingId + 10_000L;
		assertThrows(NotFoundException.class, () -> adminAccountServiceImpl.getAccountById(missingId));
	}
}
