package com.minisecutiry.member.service;

import com.minisecutiry.member.model.MiniMemberDetails;
import com.minisecutiry.member.infra.MiniMember;
import com.minisecutiry.member.infra.MiniMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MiniMemberRepository<?> memberRepository;

    @Override
    public MiniMemberDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MiniMember member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));;
        return new MiniMemberDetails(member);
    }
}
