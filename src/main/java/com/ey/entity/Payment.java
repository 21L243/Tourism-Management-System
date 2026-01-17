package com.ey.entity;

import java.time.Instant;

import com.ey.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "payments")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1, initialValue = 8001)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Booking booking;
	
	private String provider;
	private String transactionRef;
	private double amount;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status = PaymentStatus.INITIATED;
	
	private Instant paidAt;
	private Instant createdAt;
	private Instant updatedAt;

	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Instant getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Instant paidAt) {
		this.paidAt = paidAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
