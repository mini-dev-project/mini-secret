package com.minisecutiry.filter;

import com.minisecutiry.config.FilterContext;
import com.minisecutiry.config.SecurityProperties;
import com.minisecutiry.config.jwt.JwtProvider;
import com.minisecutiry.member.model.MiniMemberDetails;
import com.minisecutiry.config.cookie.CookieProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
public class SuccessHandler {

    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;
    private final SecurityProperties properties;

    public SuccessHandler(FilterContext context) {
        this.jwtProvider = context.jwtProvider();
        this.cookieProvider = context.cookieProvider();
        this.properties = context.properties();
    }

    public void successHandler(HttpServletRequest request,
                               HttpServletResponse response,
                               MiniMemberDetails memberDetails) {
        String accessToken = properties.getTokenPrefix() + buildJwtToken(memberDetails);
        response.addHeader(properties.getJwtHeaderString(), accessToken);

        String refreshToken = buildRefreshToken(memberDetails);
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
}
