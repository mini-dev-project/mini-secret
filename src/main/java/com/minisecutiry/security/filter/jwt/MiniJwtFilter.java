package com.minisecutiry.security.filter.jwt;

import com.minisecutiry.member.MiniMemberDetails;
import com.minisecutiry.security.config.MiniJwtProperties;
import com.minisecutiry.security.config.jwt.MiniJwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MiniJwtFilter extends OncePerRequestFilter {

    private final MiniJwtProvider jwtProvider;
    private final MiniJwtProperties properties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtTokenHeader = request.getHeader(properties.getJwtHeaderString());
        if (jwtTokenHeader == null
                || jwtTokenHeader.equals("null")
                || jwtTokenHeader.equals("undefined")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtTokenHeader = jwtTokenHeader.replace(properties.getTokenPrefix(), "");

        if (jwtTokenHeader.isEmpty()
                || jwtTokenHeader.equals("null")
                || jwtTokenHeader.equals("undefined")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            SecretKey jwtKey = jwtProvider.getKey(properties.getJwtSecret());
            MiniMemberDetails memberDetails = jwtProvider.parseJwtToken(jwtTokenHeader, jwtKey);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            writeErrorResponse("Token has expired");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            writeErrorResponse("Invalid JWT token");
        } catch (IllegalArgumentException e) {
            writeErrorResponse("Missing authentication token");
        } catch (Exception e) {
            writeErrorResponse("Authentication failed");
        }
        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(String message) {
        log.error("JwtFilterException: {}", message);
    }
}
