package com.ey.service;

import com.ey.dto.request.ChangePasswordRequest;
import com.ey.dto.request.ForgotPasswordRequest;
import com.ey.dto.request.LoginRequest;
import com.ey.dto.request.RegisterRequest;
import com.ey.dto.request.ResetPasswordRequest;
import com.ey.dto.response.AuthResponse;
import com.ey.dto.response.MessageResponse;

public interface AuthService {
	
	MessageResponse register(RegisterRequest req);

	AuthResponse login(LoginRequest req);

	MessageResponse changePassword(ChangePasswordRequest req);

	MessageResponse forgotPassword(ForgotPasswordRequest req);

	MessageResponse resetPassword(ResetPasswordRequest req);
}
