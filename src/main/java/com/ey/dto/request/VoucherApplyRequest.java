package com.ey.dto.request;

import jakarta.validation.constraints.NotBlank;

public class VoucherApplyRequest {
	
	@NotBlank(message="code is required")
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
