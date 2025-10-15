package com.minisecutiry.security.config;

import com.minisecutiry.member.MiniMemberDetailsService;
import com.minisecutiry.security.config.cookie.MiniCookieProvider;
import com.minisecutiry.security.config.cookie.MiniCookieProviderImpl;
import com.minisecutiry.security.config.jwt.MiniJwtProvider;
import com.minisecutiry.security.config.jwt.MiniJwtProviderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
public class MiniSecurityConfig {

    @Bean
    @ConditionalOnMissingBean(MiniJwtProvider.class)
    public MiniJwtProvider miniJwtProvider() {
        return new MiniJwtProviderImpl();
    }

    @Bean
    @ConditionalOnMissingBean(MiniCookieProvider.class)
    public MiniCookieProvider miniCookieProvider() {
        return new MiniCookieProviderImpl();
    }

    @Bean
    @ConditionalOnMissingBean(MiniJwtProperties.class)
    public MiniJwtProperties miniJwtProperties() {
        return new MiniJwtProperties();
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
