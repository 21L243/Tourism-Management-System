package com.ey.mapper;

import com.ey.dto.response.AccountDetailsResponse;
import com.ey.entity.Account;

public class AccountMapper {

	public static AccountDetailsResponse toDetails(Account req) {
		AccountDetailsResponse dto = new AccountDetailsResponse();
		dto.setId(req.getId());
		dto.setFullName(req.getFullName());
		dto.setEmail(req.getEmail());
		dto.setPhone(req.getPhone());
		dto.setRole(req.getRole());
		dto.setActive(req.isActive());
		return dto;
	}
}
