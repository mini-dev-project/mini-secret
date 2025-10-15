package com.minisecutiry.security.filter;

import com.minisecutiry.member.MiniMemberDetails;
import com.minisecutiry.security.config.cookie.MiniCookieProvider;
import com.minisecutiry.security.config.cookie.MiniCookieProviderImpl;
import com.minisecutiry.security.config.MiniJwtProperties;
import com.minisecutiry.security.config.jwt.MiniJwtProvider;
import com.minisecutiry.security.config.jwt.MiniJwtProviderImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@RequiredArgsConstructor
public class MiniLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final MiniJwtProvider jwtProvider;
    private final MiniCookieProvider cookieProvider;
    private final MiniJwtProperties properties;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException {
        MiniMemberDetails member = (MiniMemberDetails) authResult.getPrincipal();

        String accessToken = buildJwtToken(member);
        response.addHeader("accessToken",  accessToken);

        String refreshToken = buildRefreshToken(member);
        Cookie refreshTokenCookie = cookieProvider.buildCookie(properties.getRefreshHeaderString(), refreshToken, properties.getRefreshTokenExpiration());
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        response.setStatus(HttpStatus.OK.value());
    }

    private String buildJwtToken(MiniMemberDetails member) {
        SecretKey jwtKey = jwtProvider.getKey(properties.getJwtSecret());
        Claims jwtClaims = jwtProvider.createJwtClaims(member);
        return jwtProvider.createToken(jwtClaims, properties.getAccessTokenExpiration(), jwtKey);
    }

    private String buildRefreshToken(MiniMemberDetails member) {
        SecretKey refreshKey = jwtProvider.getKey(properties.getRefreshSecret());
        Claims refreshClaims = jwtProvider.createRefreshClaims(member);
        return jwtProvider.createToken(refreshClaims, properties.getAccessTokenExpiration(), refreshKey);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
    }
}
