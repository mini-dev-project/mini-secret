package com.minisecutiry.filter;

import com.minisecutiry.config.FilterContext;
import com.minisecutiry.member.model.MiniMemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final SuccessHandler successHandler;

    public OAuthSuccessHandler(FilterContext context) {
        this.successHandler = new SuccessHandler(context);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        MiniMemberDetails memberDetails = (MiniMemberDetails) authentication.getPrincipal();
        successHandler.successHandler(request, response, memberDetails);
    }
}
