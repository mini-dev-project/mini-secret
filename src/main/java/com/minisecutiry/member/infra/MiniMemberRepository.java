package com.minisecutiry.member.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface MiniMemberRepository<T extends MiniMember> extends JpaRepository<T, UUID> {
    Optional<MiniMember> findByUsername(String username);
}