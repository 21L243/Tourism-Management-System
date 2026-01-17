package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ey.dto.request.RegisterRequest;
import com.ey.dto.response.MessageResponse;
import com.ey.exception.ConflictException;
import com.ey.repository.AccountRepository;
import com.ey.repository.PasswordResetTokenRepository;
import com.ey.security.JWTUtil;
import com.ey.service.AuthServiceImpl;

@SpringBootTest
class AuthServiceImplTest {

	@Autowired
	private AuthServiceImpl authService;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JWTUtil jwtUtil;

	@MockBean
	private PasswordResetTokenRepository prtRepository;

	@Test
	void register_success() {

		RegisterRequest req = new RegisterRequest();
		req.setEmail("abc@test.com");
		req.setPassword("123");
		req.setFullName("Roopika");
		req.setPhone("99999");
		req.setRole("USER");

		when(accountRepository.existsByEmail("abc@test.com")).thenReturn(false);
		when(passwordEncoder.encode("123")).thenReturn("ENCODED");

		MessageResponse res = authService.register(req);

		assertEquals("Registered successfully", res.getMessage());
	}

	@Test
	void register_failure_emailExists() {

		RegisterRequest req = new RegisterRequest();
		req.setEmail("abc@test.com");
		req.setRole("USER");

		when(accountRepository.existsByEmail("abc@test.com")).thenReturn(true);

		assertThrows(ConflictException.class, () -> authService.register(req));
	}

}
