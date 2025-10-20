package com.minisecutiry.config;

import com.minisecutiry.config.cookie.CookieProvider;
import com.minisecutiry.config.jwt.JwtProvider;
import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;

@Builder
public record FilterContext(
        AuthenticationManager authenticationManager,
        JwtProvider jwtProvider,
        CookieProvider cookieProvider,
        SecurityProperties properties
) {}