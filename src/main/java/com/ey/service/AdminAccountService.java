package com.ey.service;

import java.util.List;

import com.ey.dto.response.AccountDetailsResponse;

public interface AdminAccountService {

	List<AccountDetailsResponse> getAllAccounts();

	AccountDetailsResponse getAccountById(Long id);

}
