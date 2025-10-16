package com.minisecutiry.member;

import com.minisecutiry.member.social.MiniOAuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class MiniMember {
    @Id
    @GeneratedValue
    private UUID id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MiniOAuthProvider provider = MiniOAuthProvider.LOCAL;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
    private String name;

    private String status;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles")
    private Set<String> roles = new HashSet<>();

    public void addRole(String role) {
        this.roles.add(role);
    }
}
