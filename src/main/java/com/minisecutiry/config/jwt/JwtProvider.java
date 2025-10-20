package com.minisecutiry.config.jwt;

import com.minisecutiry.member.model.MiniMemberDetails;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface JwtProvider {
    SecretKey getKey(String secretKey);
    Claims createJwtClaims(MiniMemberDetails memberDetails);
    Claims createRefreshClaims(MiniMemberDetails memberDetails);
    MiniMemberDetails parseJwtToken(String token, SecretKey key);
    MiniMemberDetails parseRefreshToken(String token, SecretKey key);
    String createToken(Claims claims, int validityInMilliseconds, SecretKey key);
}
