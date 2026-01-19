package com.ey.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {

	@NotNull(message="accountId is required")
	@Positive(message = "accountId must be positive")
	private Long accountId;

	@NotNull(message="tourPackageId is required")
	@Positive(message = "tourPackageId must be positive")
	private Long tourPackageId;

	@NotNull(message="startDate is required")
	private LocalDate startDate; 

	@NotNull(message="endDate is required")
	private LocalDate endDate; 

	@Min(value = 1, message = "travellersCount must be at least 1")
	private int travellersCount;

	private boolean guideRequested;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getTourPackageId() {
		return tourPackageId;
	}

	public void setTourPackageId(Long tourPackageId) {
		this.tourPackageId = tourPackageId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getTravellersCount() {
		return travellersCount;
	}

	public void setTravellersCount(int travellersCount) {
		this.travellersCount = travellersCount;
	}

	public boolean isGuideRequested() {
		return guideRequested;
	}

	public void setGuideRequested(boolean guideRequested) {
		this.guideRequested = guideRequested;
	}
	
	

	
}
