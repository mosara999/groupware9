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
     * 로그인 검증.
     * salt가 있으면 salt+SHA-256으로, 레거시(salt 없음) 계정은 salt 없는 SHA-256으로 비교하고
     * 통과하면 그 자리에서 salt를 발급해 재해시한다(rehash-on-login).
     */
    public UserVO verifyLogin(LoginVO param) {
        UserVO userInfo = sqlSession.selectOne("selectMember4Login", param.getUserid());
        if (userInfo == null) {
            return null;
        }

        String salt = userInfo.getSalt();
        if (salt != null && !salt.isEmpty()) {
            if (!PasswordUtil.matches(param.getUserpw(), salt, userInfo.getUserpw())) {
                return null;
            }
        } else {
            if (!PasswordUtil.matchesLegacy(param.getUserpw(), userInfo.getUserpw())) {
                return null;
            }
            rehashPassword(userInfo.getUserno(), param.getUserpw());
        }

        userInfo.setUserpw(null);
        userInfo.setSalt(null);
        return userInfo;
    }

    private void rehashPassword(String userno, String rawPw) {
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
