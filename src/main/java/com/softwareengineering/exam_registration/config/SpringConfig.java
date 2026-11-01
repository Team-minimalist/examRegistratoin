package com.softwareengineering.exam_registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.softwareengineering.exam_registration.security.CustomAuthenticationSuccessHandler;
import com.softwareengineering.exam_registration.security.CustomOAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SpringConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                        CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    private static final String[] PUBLIC_PATHS = {
            "/",
            "/index.html",
            "/login.html",
            "/signup.html",
            "/css/**",
            "/js/**",
            "/images/**",
            "/error",
            "/api/auth/**",
            "/oauth2/**",
            "/login/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    };

    private static final String[] ADMIN_PATHS = {
            "/admin/**",
            "/api/admin/**"
    };

    private static final String[] AUTHENTICATED_PATHS = {
            "/dashboard.html",
            "/profile.html",
            "/exams.html",
            "/exam-details.html",
            "/my-registrations.html",
            "/registration-success.html",
            "/api/students/**",
            "/api/exams/**",
            "/api/registrations/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .requestMatchers(AUTHENTICATED_PATHS).authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login.html")
                        .successHandler(customOAuth2SuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.html?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

