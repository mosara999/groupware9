package gu.member;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * COM_USER를 감싸는 스프링시큐리티 인증 주체.
 * 기존 컨트롤러/JSP가 쓰는 세션 속성(userno/userrole/usernm)을 로그인 성공 핸들러에서
 * 그대로 채워넣을 수 있도록 UserVO의 값을 그대로 노출한다.
 */
public class AppUserDetails implements UserDetails {
    private final UserVO userInfo;

    public AppUserDetails(UserVO userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserno() {
        return userInfo.getUserno();
    }

    public String getUsernm() {
        return userInfo.getUsernm();
    }

    public String getUserrole() {
        return userInfo.getUserrole();
    }

    @Override
    public String getUsername() {
        return userInfo.getUserid();
    }

    /**
     * SaltedShaPasswordEncoder가 파싱할 수 있도록 "userno$salt$hash" 형태로 결합한다
     * (salt가 없는 레거시 계정은 salt 자리를 빈 문자열로 둔다).
     */
    @Override
    public String getPassword() {
        String salt = userInfo.getSalt() == null ? "" : userInfo.getSalt();
        return userInfo.getUserno() + "$" + salt + "$" + userInfo.getUserpw();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userInfo.getUserrole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
