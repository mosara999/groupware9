package gu.member;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gu.common.PasswordUtil;
import gu.common.SearchVO;

@Service
public class MemberSvc {

    @Autowired
    private SqlSessionTemplate sqlSession;

    public Integer selectSearchMemberCount(SearchVO param) {
        return sqlSession.selectOne("selectSearchMemberCount", param);
    }

    public List<?> selectSearchMemberList(SearchVO param) {
        return sqlSession.selectList("selectSearchMemberList", param);
    }

    /**
     * 로그인용 사용자 조회(USERPW/SALT 포함). AppUserDetailsService가 사용한다.
     */
    public UserVO selectMember4Login(String userid) {
        return sqlSession.selectOne("selectMember4Login", userid);
    }

    /**
     * 레거시(salt 없음) 계정이 로그인에 성공했을 때 그 자리에서 salt를 발급해 재해시한다
     * (rehash-on-login). SaltedShaPasswordEncoder가 사용한다.
     */
    public void rehashPassword(String userno, String rawPw) {
        String newSalt = PasswordUtil.newSalt();
        UserVO update = new UserVO();
        update.setUserno(userno);
        update.setUserpw(PasswordUtil.hash(rawPw, newSalt));
        update.setSalt(newSalt);
        sqlSession.update("updateUserPassword", update);
    }

    public void insertLogIn(String param) {
        sqlSession.insert("insertLogIn", param);
    }

    public void insertLogOut(String param) {
        sqlSession.insert("insertLogOut", param);
    }
    
}
