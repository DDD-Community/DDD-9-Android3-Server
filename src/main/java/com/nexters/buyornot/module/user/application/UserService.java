package com.nexters.buyornot.module.user.application;

import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import com.nexters.buyornot.module.user.api.dto.ProfileResponse;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nexters.buyornot.global.common.codes.ErrorCode.UNAUTHORIZED_USER_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(JwtUser jwtUser) {
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new BusinessExceptionHandler(UNAUTHORIZED_USER_EXCEPTION));
        return user.getProfileResponse();
    }
}
