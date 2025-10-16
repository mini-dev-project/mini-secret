package com.minisecutiry.security.config;

import com.minisecutiry.member.MiniMemberDetailsService;
import com.minisecutiry.member.MiniMemberRepository;
import com.minisecutiry.security.config.cookie.MiniCookieProvider;
import com.minisecutiry.security.config.cookie.MiniCookieProviderBasic;
import com.minisecutiry.security.config.jwt.MiniJwtProvider;
import com.minisecutiry.security.config.jwt.MiniJwtProviderBasic;
import com.minisecutiry.security.filter.MiniFilterContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration(proxyBeanMethods = false)
public class MiniDefaultConfig {
    @Bean
    @ConditionalOnMissingBean(MiniJwtProvider.class)
    public MiniJwtProvider miniJwtProvider() {
        return new MiniJwtProviderBasic();
    }

    @Bean
    @ConditionalOnMissingBean(MiniCookieProvider.class)
    public MiniCookieProvider miniCookieProvider() {
        return new MiniCookieProviderBasic();
    }

    @Bean
    @ConditionalOnMissingBean(MiniJwtProperties.class)
    public MiniJwtProperties miniJwtProperties() {
        return new MiniJwtProperties();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(MiniMemberRepository memberRepository) {
        return new MiniMemberDetailsService(memberRepository);
    }

    @Bean
    @ConditionalOnMissingBean(MiniFilterContext.class)
    public MiniFilterContext miniFilterContext(AuthenticationConfiguration authConfig,
                                               MiniJwtProvider jwtProvider,
                                               MiniCookieProvider cookieProvider,
                                               MiniJwtProperties properties) throws Exception {
        return MiniFilterContext.builder()
                .authenticationManager(authConfig.getAuthenticationManager())
                .jwtProvider(jwtProvider)
                .cookieProvider(cookieProvider)
                .properties(properties)
                .build();
    }
}
