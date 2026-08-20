package gu.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import gu.member.MemberSvc;

/**
 * AppUserDetails.getPassword()가 내려주는 "userno$salt$hash" 문자열을 검증한다.
 * salt가 비어있는 레거시(무salt) 계정은 salt 없는 SHA-256으로 비교하고, 통과하면
 * 그 자리에서 salt를 발급해 재해시한다(rehash-on-login).
 */
@Component
public class SaltedShaPasswordEncoder implements PasswordEncoder {

    @Autowired
    private MemberSvc memberSvc;

    /**
     * DaoAuthenticationProvider가 타이밍 공격 방지용 더미 비교에 내부적으로 호출한다
     * (retrieveUser()의 prepareTimingAttackProtection()). 실제 비밀번호 저장은 UserSvc가
     * PasswordUtil로 직접 처리하므로, 이 값 자체가 어딘가에 저장되거나 재사용되지는 않는다.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        String salt = PasswordUtil.newSalt();
        return "0$" + salt + "$" + PasswordUtil.hash(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split("\\$", -1);
        if (parts.length != 3) {
            return false;
        }
        String userno = parts[0];
        String salt = parts[1];
        String hash = parts[2];

        if (!salt.isEmpty()) {
            return PasswordUtil.matches(rawPassword.toString(), salt, hash);
        }

        if (!PasswordUtil.matchesLegacy(rawPassword.toString(), hash)) {
            return false;
        }
        memberSvc.rehashPassword(userno, rawPassword.toString());
        return true;
    }
}
