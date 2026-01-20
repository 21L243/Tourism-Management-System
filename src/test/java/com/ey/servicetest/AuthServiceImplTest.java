package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ey.dto.request.RegisterRequest;
import com.ey.dto.response.MessageResponse;
import com.ey.entity.Account;
import com.ey.enums.Role;
import com.ey.exception.ConflictException;
import com.ey.repository.AccountRepository;
import com.ey.service.AuthServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class AuthServiceImplTest {

	@Autowired
	private AuthServiceImpl authService;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void register_success() {

		String email = "abc+" + UUID.randomUUID() + "@test.com";

		RegisterRequest req = new RegisterRequest();
		req.setEmail(email);
		req.setPassword("123");
		req.setFullName("Roopika");
		req.setPhone("99999");
		req.setRole("USER");

		MessageResponse res = authService.register(req);
		assertEquals("Registered successfully", res.getMessage());

		var saved = accountRepository.findByEmail(email).orElseThrow();
		assertEquals("Roopika", saved.getFullName());
		assertEquals("99999", saved.getPhone());
		assertEquals(Role.USER, saved.getRole());
	}

	@Test
	void register_failure_emailExists() {
		String email = "abc+" + UUID.randomUUID() + "@test.com";

		Account existing = new Account();
		existing.setEmail(email);
		existing.setPasswordHash("xxx");
		existing.setFullName("Ramiya");
		existing.setRole(Role.USER);
		accountRepository.save(existing);

		RegisterRequest req = new RegisterRequest();
		req.setEmail(email);
		req.setRole("USER");

		assertThrows(ConflictException.class, () -> authService.register(req));
	}
}
