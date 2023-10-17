package com.nexters.buyornot.global.config.resolver;

import com.nexters.buyornot.global.common.codes.ErrorCode;
import com.nexters.buyornot.global.exception.BusinessExceptionHandler;
import com.nexters.buyornot.global.jwt.JwtTokenProvider;
import com.nexters.buyornot.module.user.api.dto.JwtUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = JwtUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        JwtUser jwtUser;

        try {
            jwtUser = jwtTokenProvider.getJwtUser(Objects.requireNonNull(request).getHeader("Authorization"));
        } catch (NullPointerException e) {
            jwtUser = new JwtUser();
            log.info("비회원입니다. " + jwtUser.getRole());
        } catch (BusinessExceptionHandler b) {
            log.info("JWT Exception: {}", b);
            throw b;
        } catch (Exception e) {
            log.error("권한이 없는 사용자입니다.");
            throw new BusinessExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
        }

        Objects.requireNonNull(mavContainer).getModel().addAttribute("user", jwtUser);
        return jwtUser;
    }
}
