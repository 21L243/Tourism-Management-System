package com.ey.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TourPackageRequest {
	@NotBlank(message="title is required")
	private String title;
	@NotNull(message="destinationId is required")
	private Long destinationId;
	@NotBlank(message="description is required")
	private String description;
	@Positive
	private int durationDays;
	@Positive
	private double basePrice;
	@Positive(message="capacity>0")
	private int capacity;
	@NotNull(message="includes required")
	private List<String> includes;
	@NotNull(message="excludes required")
	private List<String> excludes;
	@NotBlank(message="ageEligibility required")
	private String ageEligibility;
	private boolean isActive = true;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDurationDays() {
		return durationDays;
	}
	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public List<String> getIncludes() {
		return includes;
	}
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}
	public List<String> getExcludes() {
		return excludes;
	}
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}
	public String getAgeEligibility() {
		return ageEligibility;
	}
	public void setAgeEligibility(String ageEligibility) {
		this.ageEligibility = ageEligibility;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
}
