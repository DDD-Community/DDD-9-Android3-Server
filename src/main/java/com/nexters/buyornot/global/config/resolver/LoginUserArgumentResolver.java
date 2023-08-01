package com.nexters.buyornot.global.config.resolver;

import com.nexters.buyornot.global.jwt.JwtTokenProvider;
import com.nexters.buyornot.module.user.dto.JwtUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.Objects;
import java.util.UUID;

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
        } catch (Exception e) {
            jwtUser = new JwtUser();
        }

        Objects.requireNonNull(mavContainer).getModel().addAttribute("user", jwtUser);
        return jwtUser;
    }
}
