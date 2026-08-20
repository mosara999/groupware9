package gu.config;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import gu.member.AppUserDetails;
import gu.member.LoginCtr;
import gu.member.MemberSvc;

/**
 * gu.common.LoginInterceptor/AdminInterceptor를 대체한다. 인증/인가만 담당하고
 * CSRF는 이번 범위에서 명시적으로 꺼둔다 — 이 앱은 CSRF 토큰이 전혀 없어서 jQuery AJAX POST
 * 약 34곳 + 순수 form POST 약 29곳이 한꺼번에 막히기 때문에, CSRF 적용은 별도 작업으로 분리한다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MemberSvc memberSvc;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/memberLogin", "/memberLoginChk", "/privacyPolicy", "/js/**", "/css/**", "/images/**").permitAll()
                .antMatchers("/ad*").hasAuthority("A")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/memberLogin")
                .loginProcessingUrl("/memberLoginChk")
                .usernameParameter("userid")
                .passwordParameter("userpw")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/memberLogout", "GET"))
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/noAuthMessage"))
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();

            memberSvc.insertLogIn(principal.getUserno());

            HttpSession session = request.getSession();
            session.setAttribute("userid", principal.getUsername());
            session.setAttribute("userrole", principal.getUserrole());
            session.setAttribute("userno", principal.getUserno());
            session.setAttribute("usernm", principal.getUsernm());

            if ("Y".equals(request.getParameter("remember"))) {
                LoginCtr.set_cookie("sid", principal.getUsername(), request, response);
            } else {
                LoginCtr.set_cookie("sid", "", request, response);
            }

            response.sendRedirect(request.getContextPath() + "/index");
        };
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            request.setAttribute("msg", "로그인 할 수 없습니다.");
            request.getRequestDispatcher("/WEB-INF/jsp/common/message.jsp").forward(request, response);
        };
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userno") != null) {
                memberSvc.insertLogOut(session.getAttribute("userno").toString());
            }
            response.sendRedirect(request.getContextPath() + "/memberLogin");
        };
    }
}
