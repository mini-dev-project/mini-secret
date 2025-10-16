package com.minisecutiry.member;

import com.minisecutiry.member.social.MiniGoogleOAuthMemberDetails;
import com.minisecutiry.member.social.MiniOAuthMemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiniOAuthMemberService extends DefaultOAuth2UserService {

    private final MiniMemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        MiniOAuthMemberDetails memberDetails = null;

        // 뒤에 진행할 다른 소셜 서비스 로그인을 위해 구분 => 구글
        if(provider.equals("google")){
            log.info("구글 로그인");
            memberDetails = new MiniGoogleOAuthMemberDetails(oAuth2User.getAttributes());

        }

        String providerId = memberDetails.getProviderId();
        String email = memberDetails.getEmail();
        String loginId = provider + "_" + providerId;
        String name = memberDetails.getName();

        MiniMember findMember = memberRepository.findByUsername(loginId).or;

        if (findMember == null) {
            MiniMember member = MiniMember.builder()
                    .username(loginId)
                    .name(name)
                    .build();
            memberRepository.save(member);
            return new MiniMemberDetails(member);
        }

        return new MiniMemberDetails(findMember);
    }
}
