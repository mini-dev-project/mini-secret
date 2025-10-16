package com.minisecutiry.security.filter;

import com.minisecutiry.security.config.MiniJwtProperties;
import com.minisecutiry.security.config.cookie.MiniCookieProvider;
import com.minisecutiry.security.config.jwt.MiniJwtProvider;
import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;

@Builder
public record MiniFilterContext(
        AuthenticationManager authenticationManager,
        MiniJwtProvider jwtProvider,
        MiniCookieProvider cookieProvider,
        MiniJwtProperties properties
) {}