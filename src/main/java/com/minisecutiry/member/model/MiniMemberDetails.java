package com.minisecutiry.member.model;

import com.minisecutiry.member.infra.MiniMember;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MiniMemberDetails implements UserDetails, OAuth2User {
    private final MiniMember member;

    public UUID getId() { return member.getId(); }
    public String getName() {
        return member.getName();
    }
    public String getEmail() { return member.getEmail(); }
    public String getProvider() { return member.getProvider(); }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (member.getRoles() == null || member.getRoles().isEmpty()) {
            return Set.of();
        }

        return member.getRoles().stream()
                .filter(Objects::nonNull)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return "active".equalsIgnoreCase(member.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
