package com.nexters.buyornot.global.config.web;

import com.nexters.buyornot.global.config.resolver.LoginUserArgumentResolver;
import jodd.net.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://buy-or-not-web.vercel.app")
                .allowedOrigins("https://www.web.buyornot.shop")
                .allowedOrigins("https://web.buyornot.shop")
                .allowedMethods(HttpMethod.GET.name())
                .allowedMethods(HttpMethod.PATCH.name())
                .allowedMethods(HttpMethod.POST.name());
    }
}
