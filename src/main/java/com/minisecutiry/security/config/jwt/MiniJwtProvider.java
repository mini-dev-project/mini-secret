package com.minisecutiry.security.config.jwt;

import com.minisecutiry.member.MiniMemberDetails;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface MiniJwtProvider {
    SecretKey getKey(String secretKey);
    Claims createJwtClaims(MiniMemberDetails memberDetails);
    Claims createRefreshClaims(MiniMemberDetails memberDetails);
    MiniMemberDetails parseJwtToken(String token, SecretKey key);
    MiniMemberDetails parseRefreshToken(String token, SecretKey key);
    String createToken(Claims claims, int validityInMilliseconds, SecretKey key);
}
