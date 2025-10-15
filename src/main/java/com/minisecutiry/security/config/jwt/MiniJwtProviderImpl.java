package com.minisecutiry.security.config;

import ch.qos.logback.core.net.server.Client;
import com.minisecutiry.member.MiniMember;
import com.minisecutiry.member.MiniMemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class MiniJwtProviderImpl {
    public SecretKey getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims createJwtClaims(MiniMemberDetails memberDetails) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberDetails.getId()));

        claims.put("username", memberDetails.getUsername());
        claims.put("name", memberDetails.getName());

        return claims;
    }

    public MiniMemberDetails parseJwtToken(String token, SecretKey key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        UUID id = UUID.fromString(claims.getSubject());
        MiniMember member =
                MiniMember.builder()
                        .id(id)
                        .username(claims.get("username", String.class))
                        .name(claims.get("name", String.class))
                        .build();

        return new MiniMemberDetails(member);
    }

    public Claims createRefreshClaims(MiniMemberDetails memberDetails) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberDetails.getId()));

        claims.put("username", memberDetails.getUsername());
        claims.put("name", memberDetails.getName());

        return claims;
    }

    public MiniMemberDetails parseRefreshToken(String token, SecretKey key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        UUID id = UUID.fromString(claims.getSubject());
        MiniMember member =
                MiniMember.builder()
                        .id(id)
                        .username(claims.get("username", String.class))
                        .name(claims.get("name", String.class))
                        .build();

        return new MiniMemberDetails(member);
    }

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
