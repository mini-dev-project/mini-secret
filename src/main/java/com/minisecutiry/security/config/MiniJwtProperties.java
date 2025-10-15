package com.minisecutiry.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mini-jwt")
public class MiniJwtProperties {
    private String jwtSecret;
    private String refreshSecret;

    private String jwtHeaderString = "Authorization";
    private String refreshHeaderString = "Refresh";
    private String tokenPrefix = "Bearer ";

    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    private String issuer;
    private String algorithm;
}
