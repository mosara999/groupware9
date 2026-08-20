package gu.member;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 로그인/로그아웃 처리(memberLoginChk, memberLogout)는 gu.config.SecurityConfig의
 * 스프링시큐리티 필터 체인이 담당한다 — 여기 남은 건 로그인 화면 렌더링과 "아이디 기억하기"
 * 쿠키 유틸(SecurityConfig의 성공 핸들러도 재사용)뿐이다.
 */
@Controller
public class LoginCtr {
    private static final Integer cookieExpire = 60 * 60 * 24 * 30; // 1 month

    /**
     * 로그인화면.
     */
    @RequestMapping(value = "memberLogin")
    public String memberLogin(HttpServletRequest request, ModelMap modelMap) {
        String    userid = get_cookie("sid", request);

        modelMap.addAttribute("userid", userid);

        return "member/memberLogin";
    }

    /**
     * 사용자가 관리자페이지에 접근하면 오류 출력.
     */
    @RequestMapping(value = "noAuthMessage")
    public String noAuthMessage(HttpServletRequest request) {
        return "common/noAuth";
    }
  
    /*
     * -------------------------------------------------------------------------
     */
    /**
     * 쿠키 저장.     
     */
    public static void set_cookie(String cid, String value, HttpServletRequest req, HttpServletResponse res) {

        Cookie ck = new Cookie(cid, value);
        ck.setPath("/");
        ck.setMaxAge(cookieExpire);
        ck.setHttpOnly(true);
        ck.setSecure(req.isSecure());
        res.addCookie(ck);
    }

    /**
     * 쿠키 가져오기.     
     */
    public static String get_cookie(String cid, HttpServletRequest request) {
        String ret = "";

        if (request == null) {
            return ret;
        }
        
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ret;
        }
        
        for (Cookie ck : cookies) {
            if (ck.getName().equals(cid)) {
                ret = ck.getValue();
                
                ck.setMaxAge(cookieExpire);
                break; 
            }
          }
        return ret; 
    }

}
