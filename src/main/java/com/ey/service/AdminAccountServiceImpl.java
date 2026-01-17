package com.ey.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.dto.response.AccountDetailsResponse;
import com.ey.entity.Account;
import com.ey.exception.NotFoundException;
import com.ey.mapper.AccountMapper;
import com.ey.repository.AccountRepository;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	Logger logger = LoggerFactory.getLogger(AdminAccountServiceImpl.class);


	@Override
	public AccountDetailsResponse getAccountById(Long id) {

		Account acc = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found"));
		return AccountMapper.toDetails(acc);

	}

	@Override
	public List<AccountDetailsResponse> getAllAccounts() {

		return accountRepository.findAll().stream().map(AccountMapper::toDetails).toList();

	}

	public void deleteAccountById(Long id) {
		Account acc = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found"));

		accountRepository.delete(acc);
		logger.info("Account deleted successfully");
	}

}
