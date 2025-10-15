package com.minisecutiry.security.config.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface MiniCookieProvider {
    Cookie buildCookie(String name, String value, int maxAge);
    String getCookieStringByRequest(HttpServletRequest request, String name);
}
