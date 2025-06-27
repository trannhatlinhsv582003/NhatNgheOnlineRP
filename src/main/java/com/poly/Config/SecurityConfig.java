package com.poly.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;

	// Bean để mã hóa mật khẩu
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Cấu hình phân quyền & login
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/product/**", "/css/**", "/js/**", "/images/**", "/register", "/forgot-password",
						"/api/**", "/components/**", "/libraries/**", "/search/**", "/auth/**")
				.permitAll().requestMatchers("/checkout/**", "/user/**", "/cart/**").authenticated().anyRequest()
				.authenticated())
				.formLogin(form -> form.loginPage("/").loginProcessingUrl("/login").usernameParameter("email")
						.passwordParameter("password").successHandler((req, res, auth) -> {
							res.setStatus(200);
						}).failureHandler((req, res, ex) -> {
							res.setStatus(401);
							res.setContentType("application/json");
							res.setCharacterEncoding("UTF-8"); // Bổ sung quan trọng
							res.getWriter().write("{\"error\":\"Email hoặc mật khẩu không đúng\"}");
						}).permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/").permitAll());

		return http.build();
	}

}
