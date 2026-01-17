package com.ey.service;

import java.util.List;

import com.ey.dto.request.FeedbackRequest;
import com.ey.entity.Feedback;

public interface FeedbackService {
	Feedback submit(FeedbackRequest req);

	List<Feedback> list();
}