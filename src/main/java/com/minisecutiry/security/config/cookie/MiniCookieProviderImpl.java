package com.minisecutiry.security.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MiniCookieProviderImpl {
    public Cookie buildCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public String getCookieStringByRequest(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
