package com.htc.licenseapproval.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.htc.licenseapproval.service.UserService;
import com.htc.licenseapproval.service.jpaauditing.ApplicationAuditAware;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	@Lazy
	UserService userService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(cors -> {
            cors.configurationSource(corsConfigurationSource());
        })    
		     
				.authorizeHttpRequests(
						authz -> authz
								.requestMatchers("/auth/**", 
								        "/swagger-ui/**", "/v3/api-docs/**","/v3/api-docs", 
								        "/swagger-resources/**", "/webjars/**")
								.permitAll()
								.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//				.formLogin(form -> form.loginPage("/auth/login").loginProcessingUrl("/auth/login"))	
//				.logout(logout -> logout.invalidateHttpSession(true).clearAuthentication(true)
//						.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")))
//				.authenticationProvider(authenticationProvider())
//				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
//				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean	
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	 CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.addAllowedOrigin("http://localhost:5173"); 
	    configuration.addAllowedMethod("*"); 
	    configuration.addAllowedHeader("*"); 
	    configuration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new 
	     UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	@Bean
    public AuditorAware<String> auditorAware(){
		return new ApplicationAuditAware();
	}
	
}