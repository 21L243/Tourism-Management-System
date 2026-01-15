package com.ey.config;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ey.entity.Account;
import com.ey.repository.AccountRepository;

@Component
public class CurrentUserUtil {

	private final AccountRepository accountRepository;

	public CurrentUserUtil(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Optional<Account> getCurrentAccount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}
		String email = auth.getName();
		return accountRepository.findByEmail(email);
	}

	public Optional<String> getCurrentEmail() {
		return getCurrentAccount().map(Account::getEmail);
	}
}
