package gu.config;

import gu.common.AdminInterceptor;
import gu.common.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Java-config replacement for dispatcher-servlet.xml's {@code <mvc:interceptors>}/
 * {@code <mvc:resources>}. Those XML namespace elements register shared MVC infrastructure
 * beans (mvcUrlPathHelper, mvcContentNegotiationManager, ...) under the same names Spring Boot's
 * WebMvcAutoConfiguration uses, which fails startup with a bean-definition-override error — plain
 * WebMvcConfigurer callbacks avoid that.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/memberLogin", "/memberLoginChk", "/privacyPolicy", "/js/**", "/css/**", "/images/**");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/ad*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
    }
}
