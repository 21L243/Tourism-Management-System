package com.ey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.AccountDetailsResponse;
import com.ey.service.AdminAccountService;

@RestController
@RequestMapping("/api/v1/admin/accounts")
public class AdminAccountController {

	@Autowired
	private AdminAccountService adminAccountService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<AccountDetailsResponse> getAllAccounts() {
		return adminAccountService.getAllAccounts();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public AccountDetailsResponse getAccountById(@PathVariable Long id) {
		return adminAccountService.getAccountById(id);
	}
}
