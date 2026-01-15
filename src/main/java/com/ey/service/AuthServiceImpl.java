package com.ey.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.ey.dto.request.ChangePasswordRequest;
import com.ey.dto.request.ForgotPasswordRequest;
import com.ey.dto.request.LoginRequest;
import com.ey.dto.request.RegisterRequest;
import com.ey.dto.request.ResetPasswordRequest;
import com.ey.dto.response.AuthResponse;
import com.ey.dto.response.MessageResponse;
import com.ey.entity.Account;
import com.ey.entity.PasswordResetToken;
import com.ey.enums.Role;
import com.ey.exception.BadRequestException;
import com.ey.exception.ConflictException;
import com.ey.exception.NotFoundException;
import com.ey.repository.AccountRepository;
import com.ey.repository.PasswordResetTokenRepository;
import com.ey.security.JWTUtil;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private PasswordResetTokenRepository prtRepository;

	@Override
	public MessageResponse register(RegisterRequest req) {
		if (accountRepository.existsByEmail(req.getEmail())) {
			throw new ConflictException("Email already exists");
		}

		Role role;
		try {
			role = Role.valueOf(req.getRole().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Invalid role: " + req.getRole());
		}


		Account acc = new Account();
		acc.setFullName(req.getFullName());
		acc.setEmail(req.getEmail());
		acc.setPasswordHash(passwordEncoder.encode(req.getPassword()));
		acc.setPhone(req.getPhone());
		acc.setRole(role);
		acc.setActive(true); 

		accountRepository.save(acc);

		return new MessageResponse("Registered successfully");
	}

	@Override
	public AuthResponse login(LoginRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
		
		String token = jwtUtil.generateToken(req.getEmail());
		return new AuthResponse(token);
	}

	@Override
	public MessageResponse changePassword(ChangePasswordRequest req) {
		Account acc = accountRepository.findByEmail(req.getEmail())
				.orElseThrow(() -> new NotFoundException("Account not found"));

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getCurrentPassword()));

		acc.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
		accountRepository.save(acc);

		return new MessageResponse("Password changed successfully");
	}

	@Override
	public MessageResponse forgotPassword(ForgotPasswordRequest req) {
		Account acc = accountRepository.findByEmail(req.getEmail())
				.orElseThrow(() -> new NotFoundException("Email doesn't exist"));

		String token = UUID.randomUUID().toString();

		
		PasswordResetToken prt = new PasswordResetToken();
		prt.setToken(token);
		prt.setAccount(acc);
		prt.setExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES));
		prt.setUsed(false);

		prtRepository.save(prt);

		
		return new MessageResponse("Use token: " + token);
	}

	@Override
	public MessageResponse resetPassword(ResetPasswordRequest req) {
		Account acc = accountRepository.findByEmail(req.getEmail())
				.orElseThrow(() -> new NotFoundException("Account not found"));

		PasswordResetToken prt = prtRepository.findByToken(req.getToken())
				.orElseThrow(() -> new BadRequestException("Invalid token"));

		boolean invalid = prt.isUsed() || prt.getExpiresAt().isBefore(Instant.now())
				|| !prt.getAccount().getId().equals(acc.getId());

		if (invalid) {
			throw new BadRequestException("Invalid token");
		}

		acc.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
		prt.setUsed(true);

		accountRepository.save(acc);
		prtRepository.save(prt);

		return new MessageResponse("Password has been reset successfully.");
	}
}
