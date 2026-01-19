package com.ey.dto.request;

import jakarta.validation.constraints.NotBlank;

public class BookingStatusUpdateRequest {
	
	@NotBlank(message="status required")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
