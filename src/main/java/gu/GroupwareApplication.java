package gu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;

/**
 * Replaces web.xml: DispatcherServlet/root context registration is handled by
 * {@code @SpringBootApplication}, and the legacy bean wiring is pulled in as-is via
 * {@code @ImportResource} rather than rewritten into Java config.
 */
@SpringBootApplication
@ImportResource({
        "classpath:spring/applicationContext.xml",
        "classpath:spring/dispatcher-servlet.xml"
})
public class GroupwareApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GroupwareApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GroupwareApplication.class);
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                registry.addErrorPages(
                        new ErrorPage(HttpStatus.NOT_FOUND, "/WEB-INF/jsp/common/error404.jsp"),
                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/WEB-INF/jsp/common/error500.jsp"),
                        new ErrorPage(Exception.class, "/WEB-INF/jsp/common/error.jsp"));
            }
        };
    }
}
