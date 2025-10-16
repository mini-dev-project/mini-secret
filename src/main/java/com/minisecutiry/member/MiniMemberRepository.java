package com.minisecutiry.member;

import java.util.Optional;

public interface MiniMemberRepository {
    Optional<MiniMember> findByUsername(String username);
    void save(MiniMember member);
}