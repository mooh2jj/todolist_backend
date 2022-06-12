package com.example.todolist_prac.service.user;

import com.example.todolist_prac.components.oauth2.FacebookUserInfo;
import com.example.todolist_prac.components.oauth2.GoogleUserInfo;
import com.example.todolist_prac.components.oauth2.OAuth2UserInfo;
import com.example.todolist_prac.model.user.User;
import com.example.todolist_prac.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest: {}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        log.info("clientRegistration :{}", clientRegistration);
        OAuth2User oAuth2User = super.loadUser(userRequest);

        oAuth2User.getAuthorities().forEach((k) -> {
            log.info("k: {}", k);
        });

        // oauth 회원가입 강제 등록
        OAuth2UserInfo oAuth2UserInfo = null;

        if (clientRegistration.getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            log.info("oAuth2UserInfo: {}",oAuth2UserInfo);
        } else if(clientRegistration.getRegistrationId().equals("facebook")){
            log.info("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            log.info("oAuth2UserInfo: {}",oAuth2UserInfo);
        }else{
            log.info("우리는 구글과 페이스북만 지원합니다.");
        }

        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        log.info("email: {}", email);
        log.info("name: {}", name);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = null;
        if (optionalUser.isPresent()) {
            log.info("로그인을 이미 했음, 자동회원가입이 되어있다.");
        } else {
            user = User.builder()
                    .name(name)
                    .email(email)
                    .password("githere")
                    .build();

            userRepository.save(user);
//            return new PrincipalDetails(user, oAuth2User.getAttributes());    // TODO: NPE 발생

        }
        return oAuth2User;
    }
}
