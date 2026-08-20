package gu.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PrivacyCtr {

    /**
     * 개인정보 처리방침.
     */
    @RequestMapping(value = "/privacyPolicy")
    public String privacyPolicy() {
        return "common/PrivacyPolicy";
    }

}
