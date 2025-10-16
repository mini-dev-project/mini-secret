package com.minisecutiry.member.social;

public interface MiniOAuthMemberDetails {
    MiniOAuthProvider getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
