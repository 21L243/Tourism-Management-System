package com.ey.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ey.repository.AccountRepository;
import com.ey.security.JWTAuthenticationFilter;
import com.ey.security.JWTAuthorizationFilter;
import com.ey.security.JWTUtil;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final JWTUtil jwtUtil;

	public SecurityConfig(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public UserDetailsService userDetailsService(AccountRepository accountRepository) {
		return email -> accountRepository.findByEmail(email)
				.map(acc -> org.springframework.security.core.userdetails.User.withUsername(acc.getEmail())
						.password(acc.getPasswordHash()).authorities("ROLE_" + acc.getRole())
						.accountLocked(!acc.isActive()).disabled(!acc.isActive()).build())
				.orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
						"Account not found: " + email));
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
		return cfg.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService, AccountRepository accountRepository) throws Exception {

		var jwtAuthFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);
		jwtAuthFilter.setFilterProcessesUrl("/api/v1/auth/login");

		http.csrf(csrf -> csrf.disable())
				.sessionManagement(
						sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
								.requestMatchers("/api/v1/auth/forgot-password", "/api/v1/auth/reset-password")
								.permitAll()
								.requestMatchers(HttpMethod.GET, "/api/v1/destinations/**").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/v1/packages/**").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/v1/trip-plans/**").permitAll()
								.requestMatchers(HttpMethod.GET, "/api/v1/guides/**").permitAll()
								.requestMatchers("/api/v1/auth/change-password").authenticated()
								.requestMatchers("/api/v1/bookings/**", "/api/v1/payments/**", "/api/v1/feedback/**",
										"/api/v1/vouchers/**", "/api/v1/user/**")
								.hasAnyRole("USER", "ADMIN")
								.requestMatchers(HttpMethod.POST, "/api/v1/destinations/**", "/api/v1/packages/**",
										"/api/v1/guides/**")
								.hasRole("ADMIN")
								.requestMatchers(HttpMethod.PUT, "/api/v1/destinations/**", "/api/v1/packages/**",
										"/api/v1/trip-plans/**", "/api/v1/guides/**", "/api/v1/bookings/**")
								.hasRole("ADMIN")
								.requestMatchers(HttpMethod.DELETE, "/api/v1/destinations/**", "/api/v1/packages/**",
										"/api/v1/trip-plans/**", "/api/v1/guides/**")
								.hasRole("ADMIN").anyRequest().authenticated())
				.formLogin(form -> form.disable())
				.addFilterAt(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthorizationFilter(jwtUtil, userDetailsService),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
