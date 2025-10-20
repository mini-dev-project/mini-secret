package com.minisecutiry.config.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface CookieProvider {
    Cookie buildCookie(String name, String value, int maxAge);
    String getCookieStringByRequest(HttpServletRequest request, String name);
}
