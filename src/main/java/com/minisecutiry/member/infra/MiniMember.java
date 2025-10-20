package com.minisecutiry.member.infra;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class MiniMember {

    protected MiniMember(MiniMember miniMember) {
        this.id = miniMember.id;
        this.username = miniMember.username;
        this.password = miniMember.password;
        this.provider = miniMember.provider;

        this.name = miniMember.name;
        this.email = miniMember.email;

        this.status = Optional.ofNullable(miniMember.status).orElse("ACTIVE");
        this.roles = miniMember.roles;
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    @Builder.Default
    @Column(updatable = false)
    private String provider = "local";

    private String name;
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private String status = "active";

    public void updateStatus(String status) {
        this.status = status;
    }

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles")
    private Set<String> roles = new HashSet<>();

    public void addRole(String role) {
        this.roles.add(role);
    }
}
