package gu.admin.organ;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gu.common.PasswordUtil;
import gu.common.SearchVO;
import gu.member.UserVO;

@Service
public class UserSvc {

    @Autowired
    private SqlSessionTemplate sqlSession;    
        
    public List<?> selectUserList(String param) {
        return sqlSession.selectList("selectUserList", param);
    }
    
    public List<?> selectUserListWithDept(SearchVO param) {
        return sqlSession.selectList("selectUserListWithDept", param);
    }
    
    /**
     * 사용자 저장.     
     */
    public void insertUser(UserVO param) {
        if (param.getUserno() == null || "".equals(param.getUserno())) {
             String salt = PasswordUtil.newSalt();
             param.setSalt(salt);
             param.setUserpw(PasswordUtil.hash(param.getUserpw(), salt));
             sqlSession.insert("insertUser", param);
        } else {
            sqlSession.insert("updateUser", param);
        }
    }
    
    public String selectUserID(String param) {
        return sqlSession.selectOne("selectUserID", param);
    }

    public UserVO selectUserOne(String param) {
        return sqlSession.selectOne("selectUserOne", param);
    }
    
    public void deleteUser(String param) {
        sqlSession.delete("deleteUser", param);
    }

    /*
     * 사용자 
     */
    public void updateUserByMe(UserVO param) {
        sqlSession.delete("updateUserByMe", param);
    }
    
    public void updateUserPassword(UserVO param) {
        String salt = PasswordUtil.newSalt();
        param.setSalt(salt);
        param.setUserpw(PasswordUtil.hash(param.getUserpw(), salt));
        sqlSession.update("updateUserPassword", param);
    }

    /**
     * 본인 비밀번호 변경 전 현재 비밀번호 확인.
     */
    public boolean verifyPassword(String userno, String rawPw) {
        UserVO stored = sqlSession.selectOne("selectUserPasswordByUserno", userno);
        if (stored == null) {
            return false;
        }
        String salt = stored.getSalt();
        if (salt != null && !salt.isEmpty()) {
            return PasswordUtil.matches(rawPw, salt, stored.getUserpw());
        }
        return PasswordUtil.matchesLegacy(rawPw, stored.getUserpw());
    }

}
