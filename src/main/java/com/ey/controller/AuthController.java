package com.ey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.ChangePasswordRequest;
import com.ey.dto.request.ForgotPasswordRequest;
import com.ey.dto.request.LoginRequest;
import com.ey.dto.request.RegisterRequest;
import com.ey.dto.request.ResetPasswordRequest;
import com.ey.dto.request.UpdateProfileRequest;
import com.ey.dto.response.AccountDetailsResponse;
import com.ey.dto.response.AuthResponse;
import com.ey.dto.response.MessageResponse;
import com.ey.entity.Account;
import com.ey.exception.BadRequestException;
import com.ey.exception.NotFoundException;
import com.ey.mapper.AccountMapper;
import com.ey.repository.AccountRepository;
import com.ey.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private AccountRepository accountRepository;

	@PostMapping("/register")
	public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest req) {
		return ResponseEntity.status(201).body(authService.register(req));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
		return ResponseEntity.ok(authService.login(req));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<MessageResponse> forgot(@Valid @RequestBody ForgotPasswordRequest req) {
		return ResponseEntity.ok(authService.forgotPassword(req));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<MessageResponse> reset(@Valid @RequestBody ResetPasswordRequest req) {
		return ResponseEntity.ok(authService.resetPassword(req));
	}

	@PutMapping("/change-password")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<MessageResponse> change(@Valid @RequestBody ChangePasswordRequest req) {
		return ResponseEntity.ok(authService.changePassword(req));
	}

	@GetMapping
	public ResponseEntity<AccountDetailsResponse> getCurrentUser(Authentication authentication) {
		String email = authentication.getName();
		Account acc = accountRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Account not found"));
		return ResponseEntity.ok(AccountMapper.toDetails(acc));
	}

	@PutMapping
	public ResponseEntity<AccountDetailsResponse> updateCurrentUser(Authentication authentication,
			@Valid @RequestBody UpdateProfileRequest req) {
		String currentEmail = authentication.getName();

		Account acc = accountRepository.findByEmail(currentEmail)
				.orElseThrow(() -> new NotFoundException("Account not found"));

		if (!acc.getEmail().equalsIgnoreCase(req.getEmail())
				&& accountRepository.existsByEmailAndIdNot(req.getEmail(), acc.getId())) {

			throw new BadRequestException("Email is already in use");
		}

		acc.setFullName(req.getFullName());
		acc.setEmail(req.getEmail());
		acc.setPhone(req.getPhone());

		Account saved = accountRepository.save(acc);
		return ResponseEntity.ok(AccountMapper.toDetails(saved));
	}

	@PutMapping("/deactivate")
	public ResponseEntity<MessageResponse> deactivateCurrentUser(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(401).body(new MessageResponse("Unauthorized"));
		}

		String email = authentication.getName();

		Account acc = accountRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Account not found"));

		if (!acc.isActive()) {
			return ResponseEntity.ok(new MessageResponse("Account is already deactivated"));
		}

		acc.setActive(false);
		accountRepository.save(acc);

		return ResponseEntity.ok(new MessageResponse("Account deactivated successfully"));
	}

}
