package com.nexters.buyornot.module.auth.application;

import com.nexters.buyornot.global.common.codes.ErrorCode;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.auth.api.dto.request.KakaoLoginParams;
import com.nexters.buyornot.module.auth.api.dto.response.AuthTokens;
import com.nexters.buyornot.module.auth.event.DeletedUserEvent;
import com.nexters.buyornot.module.auth.model.AuthTokensGenerator;
import com.nexters.buyornot.module.auth.model.oauth.OAuthInfoResponse;
import com.nexters.buyornot.module.auth.model.oauth.RequestOAuthInfoService;
import com.nexters.buyornot.module.model.Role;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.Nickname;
import com.nexters.buyornot.module.user.domain.User;
import com.nexters.buyornot.module.user.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private static final String path = "resources/profile";
    private static final String EXTENSION = ".png";
    private static final String PREFIX = "익명의";

    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final ApplicationEventPublisher eventPublisher;

    public AuthTokens login(KakaoLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        JwtUser user = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(user);
    }

    private JwtUser findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::toJwtUser)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private JwtUser newMember(OAuthInfoResponse oAuthInfoResponse) {

        String email = oAuthInfoResponse.getEmail();
        if(email.isEmpty()) email = "";

        List<String> list = generateNickname();
        String nickname = list.get(0);
        String profile = list.get(1);

        while(userRepository.existsByNickname(nickname)) {
            list = generateNickname();
            nickname = list.get(0);
            profile = list.get(1);
        }

        User user = User.builder()
                .name(oAuthInfoResponse.getNickname())
                .gender(oAuthInfoResponse.getGender())
                .email(email)
                .nickname(nickname)
                .profile(profile)
                .ageRange(oAuthInfoResponse.getAgeRange())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        return savedUser.toJwtUser();
    }

    private List<String> generateNickname() {
        double min = 0;
        double max = 19;
        Random rand = new Random();

        int random = (int) ((Math.random() * (max - min)) + min);
        int num = rand.nextInt(10000);

        Nickname[] arr = Nickname.values();
        Nickname profile = arr[random];

        String pic = path + "/" + profile.getValue() + EXTENSION;
        String nickname = PREFIX + profile.getValue() + num;

        List<String> list = new ArrayList<>();
        list.add(nickname);
        list.add(pic);
        return list;
    }

    @Transactional
    public String logout(JwtUser jwtUser) {
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION));

        authTokensGenerator.expireToken(user.getId());
        return "logout success";
    }

    @Transactional
    public String signOut(JwtUser jwtUser) {
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION));
        user.delete();
        eventPublisher.publishEvent(DeletedUserEvent.of(user));
        return user.getName() + " sign out success";
    }
}
