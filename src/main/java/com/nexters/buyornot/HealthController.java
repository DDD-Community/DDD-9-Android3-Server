package com.nexters.buyornot;

import com.nexters.buyornot.global.common.codes.SuccessCode;
import com.nexters.buyornot.global.common.response.ApiResponse;
import com.nexters.buyornot.module.user.dao.UserRepository;
import com.nexters.buyornot.module.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthController {

    private final UserRepository userRepository;

    @RequestMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() throws Exception {
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, "health check OK");
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        User user = new User("mina");
        User newUser = userRepository.save(user);
        return ApiResponse.success(SuccessCode.SELECT_SUCCESS, "id: " + newUser.getId() + " Name: " + newUser.getName());
    }

}
