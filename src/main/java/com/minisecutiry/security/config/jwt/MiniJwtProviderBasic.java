package com.minisecutiry.security.config.jwt;

import com.minisecutiry.member.MiniMember;
import com.minisecutiry.member.MiniMemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class MiniJwtProviderBasic implements MiniJwtProvider {
    @Override
    public SecretKey getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Claims createJwtClaims(MiniMemberDetails memberDetails) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberDetails.getId()));

        claims.put("username", memberDetails.getUsername());
        claims.put("name", memberDetails.getName());

        Set<String> roles = memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        if (!roles.isEmpty()) {
            claims.put("roles", roles);
        }

        return claims;
    }

    @Override
    public MiniMemberDetails parseJwtToken(String token, SecretKey key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        UUID id = UUID.fromString(claims.getSubject());

        @SuppressWarnings("unchecked")
        Set<String> roles = new HashSet<>((List<String>) claims.get("roles", List.class));

        MiniMember member =
                MiniMember.builder()
                        .id(id)
                        .username(claims.get("username", String.class))
                        .name(claims.get("name", String.class))
                        .roles(roles)
                        .build();

        return new MiniMemberDetails(member);
    }

    @Override
    public Claims createRefreshClaims(MiniMemberDetails memberDetails) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberDetails.getId()));

        claims.put("username", memberDetails.getUsername());
        claims.put("name", memberDetails.getName());

        Set<String> roles = memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        if (!roles.isEmpty()) {
            claims.put("roles", roles);
        }

        return claims;
    }

    @Override
    public MiniMemberDetails parseRefreshToken(String token, SecretKey key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        UUID id = UUID.fromString(claims.getSubject());

        @SuppressWarnings("unchecked")
        Set<String> roles = new HashSet<>((List<String>) claims.get("roles", List.class));

        MiniMember member =
                MiniMember.builder()
                        .id(id)
                        .username(claims.get("username", String.class))
                        .name(claims.get("name", String.class))
                        .roles(roles)
                        .build();

        return new MiniMemberDetails(member);
    }

    @Override
    public String createToken(
            Claims claims, int validityInMilliseconds, SecretKey key) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }
}
