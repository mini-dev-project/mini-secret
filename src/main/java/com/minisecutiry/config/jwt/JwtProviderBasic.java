package com.minisecutiry.config.jwt;

import com.minisecutiry.member.infra.MiniMember;
import com.minisecutiry.member.model.MiniMemberDetails;
import io.jsonwebtoken.Claims;

public class JwtProviderBasic extends JwtProvider {
    @Override
    public MiniMember buildMiniMember(Claims claims) {
        return null;
    }

    @Override
    public Claims buildClaim(MiniMemberDetails memberDetails) {
        return null;
    }
}
