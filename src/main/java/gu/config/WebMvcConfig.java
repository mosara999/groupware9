package gu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Java-config replacement for dispatcher-servlet.xml's {@code <mvc:resources>}. That XML
 * namespace element registers shared MVC infrastructure beans (mvcUrlPathHelper,
 * mvcContentNegotiationManager, ...) under the same names Spring Boot's WebMvcAutoConfiguration
 * uses, which fails startup with a bean-definition-override error — a plain WebMvcConfigurer
 * callback avoids that.
 *
 * Login/admin access control used to live here as HandlerInterceptors; that's now handled by
 * gu.config.SecurityConfig's Spring Security filter chain instead.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
    }
}
