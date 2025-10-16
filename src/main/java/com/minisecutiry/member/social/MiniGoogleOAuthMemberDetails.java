package com.minisecutiry.member.social;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class MiniGoogleOAuthMemberDetails implements MiniOAuthMemberDetails {
    private Map<String, Object> attributes;

    @Override
    public MiniOAuthProvider getProvider() {
        return MiniOAuthProvider.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
