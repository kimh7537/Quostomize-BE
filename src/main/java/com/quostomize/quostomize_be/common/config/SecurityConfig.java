package com.quostomize.quostomize_be.common.config;

import com.quostomize.quostomize_be.common.error.handler.CustomAccessDeniedHandler;
import com.quostomize.quostomize_be.common.filter.JwtAuthenticationFilter;
import com.quostomize.quostomize_be.common.filter.JwtAuthorizationFilter;
import com.quostomize.quostomize_be.common.filter.JwtExceptionFilter;
import com.quostomize.quostomize_be.common.jwt.JwtTokenProvider;
import com.quostomize.quostomize_be.common.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/v1/api/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
//            "/**"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CorsFilter corsFilter;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http.authenticationManager(authenticationManager);

        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/v1/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                );

        // Custom Filter 추가
        http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider, refreshTokenRepository))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(corsFilter, JwtExceptionFilter.class);

        return http.build();
    }
}