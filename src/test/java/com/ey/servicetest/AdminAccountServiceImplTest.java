package com.ey.servicetest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ey.dto.response.AccountDetailsResponse;
import com.ey.entity.Account;
import com.ey.exception.NotFoundException;
import com.ey.repository.AccountRepository;
import com.ey.service.AdminAccountServiceImpl;

@SpringBootTest
class AdminAccountServiceImplTest {

	@Autowired
	private AdminAccountServiceImpl adminAccountServiceImpl;

	@MockBean // fake repository (no DB)
	private AccountRepository accountRepository;

	@Test
	void getAccountById_success() {

		Account acc = new Account();
		acc.setId(1L);
		
		when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));

		AccountDetailsResponse response = adminAccountServiceImpl.getAccountById(1L);

		assertNotNull(response);
	}

	@Test
	void getAccountById_failure() {

		when(accountRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> adminAccountServiceImpl.getAccountById(1L));

	}

}
