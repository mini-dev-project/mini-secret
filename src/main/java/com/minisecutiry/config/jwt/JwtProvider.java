package com.minisecutiry.config.jwt;

import com.minisecutiry.member.infra.MiniMember;
import com.minisecutiry.member.model.MiniMemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class JwtProvider {
    public SecretKey getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims createJwtClaims(MiniMemberDetails memberDetails) {
        return buildJwtClaim(memberDetails);
    }

    public Claims buildJwtClaim(MiniMemberDetails memberDetails) {
        return buildClaim(memberDetails);
    }

    public MiniMemberDetails parseJwtToken(String token, SecretKey key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        MiniMember member = buildJwtMiniMember(claims);
        return new MiniMemberDetails(member);
    }

    public MiniMember buildJwtMiniMember(Claims claims) {
        return buildMiniMember(claims);
    }


    public Claims createRefreshClaims(MiniMemberDetails memberDetails) {
        return buildRefreshClaim(memberDetails);
    }

    public Claims buildRefreshClaim(MiniMemberDetails memberDetails) {
        return buildClaim(memberDetails);
    }

    public MiniMemberDetails parseRefreshToken(String token, SecretKey key) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        MiniMember member = buildRefreshMiniMember(claims);
        return new MiniMemberDetails(member);
    }

    public MiniMember buildRefreshMiniMember(Claims claims) {
        return buildMiniMember(claims);
    }

    public abstract MiniMember buildMiniMember(Claims claims);
    public abstract Claims buildClaim(MiniMemberDetails memberDetails);

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
