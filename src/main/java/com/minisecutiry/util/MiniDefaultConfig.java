package com.minisecutiry.util;

import com.minisecutiry.config.FilterContext;
import com.minisecutiry.config.SecurityProperties;
import com.minisecutiry.filter.OAuthSuccessHandler;
import com.minisecutiry.member.service.MemberDetailsService;
import com.minisecutiry.member.infra.MiniMemberRepository;
import com.minisecutiry.config.cookie.CookieProvider;
import com.minisecutiry.config.cookie.CookieProviderBasic;
import com.minisecutiry.config.jwt.JwtProvider;
import com.minisecutiry.config.jwt.JwtProviderBasic;
import com.minisecutiry.member.service.OAuthMemberSaver;
import com.minisecutiry.member.service.OAuthMemberDetailsService;
import com.minisecutiry.member.service.OAuthMemberSaverBasic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration(proxyBeanMethods = false)
public class MiniDefaultConfig {
    @Bean
    @ConditionalOnMissingBean(JwtProvider.class)
    public JwtProvider miniJwtProvider() {
        return new JwtProviderBasic();
    }

    @Bean
    @ConditionalOnMissingBean(CookieProvider.class)
    public CookieProvider miniCookieProvider() {
        return new CookieProviderBasic();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityProperties.class)
    public SecurityProperties miniJwtProperties() {
        return new SecurityProperties();
    }

    @Bean
    @ConditionalOnBean(MiniMemberRepository.class)
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(MiniMemberRepository<?> memberRepository) {
        return new MemberDetailsService(memberRepository);
    }

    @Bean
    @ConditionalOnMissingBean(FilterContext.class)
    public FilterContext miniFilterContext(AuthenticationConfiguration authConfig,
                                           JwtProvider jwtProvider,
                                           CookieProvider cookieProvider,
                                           SecurityProperties properties) throws Exception {
        return FilterContext.builder()
                .authenticationManager(authConfig.getAuthenticationManager())
                .jwtProvider(jwtProvider)
                .cookieProvider(cookieProvider)
                .properties(properties)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(SimpleUrlAuthenticationSuccessHandler.class)
    public SimpleUrlAuthenticationSuccessHandler miniOAuthSuccessHandler(FilterContext context) {
        return new OAuthSuccessHandler(context);
    }

    @Bean
    @ConditionalOnMissingBean(OAuthMemberSaver.class)
    public OAuthMemberSaver oAuthMemberSaver() {
        return new OAuthMemberSaverBasic();
    }

    @Bean
    @ConditionalOnBean(MiniMemberRepository.class)
    @ConditionalOnMissingBean(DefaultOAuth2UserService.class)
    public DefaultOAuth2UserService defaultOAuth2UserService(MiniMemberRepository<?> memberRepository,
                                                             OAuthMemberSaver memberSaver) {
        return new OAuthMemberDetailsService(memberRepository, memberSaver);
    }
}
