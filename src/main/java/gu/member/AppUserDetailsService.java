package gu.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberSvc memberSvc;

    @Override
    public UserDetails loadUserByUsername(String userid) {
        UserVO userInfo = memberSvc.selectMember4Login(userid);
        if (userInfo == null) {
            throw new UsernameNotFoundException(userid);
        }
        return new AppUserDetails(userInfo);
    }
}
