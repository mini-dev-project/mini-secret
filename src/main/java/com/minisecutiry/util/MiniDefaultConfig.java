package com.minisecutiry.util;

import com.minisecutiry.config.FilterContext;
import com.minisecutiry.config.SecurityProperties;
import com.minisecutiry.config.jwt.JwtProviderBasic;
import com.minisecutiry.filter.OAuthSuccessHandler;
import com.minisecutiry.config.cookie.CookieProvider;
import com.minisecutiry.config.cookie.CookieProviderBasic;
import com.minisecutiry.config.jwt.JwtProvider;
import com.minisecutiry.filter.SuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    @ConditionalOnMissingBean(SuccessHandler.class)
    public SuccessHandler miniSuccessHandler(FilterContext context) {
        return new SuccessHandler(context);
    }
}
