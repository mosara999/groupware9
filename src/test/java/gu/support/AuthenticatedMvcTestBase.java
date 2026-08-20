package gu.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

/**
 * View/controller 테스트 공용 베이스. 이 앱은 로그인 성공 핸들러(SecurityConfig)가
 * Authentication이 아니라 HttpSession 속성(userno/userrole/usernm/userid)을 직접 채워서
 * 컨트롤러/뷰가 그걸 읽으므로, @WithMockUser로는 세션 속성이 비어 테스트 대상 화면이
 * NPE를 낸다 - 실제 로그인 폼과 동일하게 /memberLoginChk를 거쳐야 한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AuthenticatedMvcTestBase {

    protected static final String TEST_USERID = "admin";
    protected static final String TEST_USERPW = "admin";

    @Autowired
    protected MockMvc mockMvc;

    protected MockHttpSession session;
    protected String loginUserno;

    @BeforeEach
    void loginAsAdmin() throws Exception {
        MockHttpSession freshSession = new MockHttpSession();

        mockMvc.perform(post("/memberLoginChk")
                        .session(freshSession)
                        .param("userid", TEST_USERID)
                        .param("userpw", TEST_USERPW))
                .andExpect(status().is3xxRedirection());

        this.session = freshSession;
        this.loginUserno = (String) freshSession.getAttribute("userno");
    }
}
