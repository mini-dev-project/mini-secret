package com.minisecutiry.member.social;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MiniOAuthProvider {
    GOOGLE("google"),
    LOCAL("local");
    private final String name;
}
