package com.demo.springsecuritydemo.detail;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 继承自webAuthenticationDetail
 *
 * @author suvue
 * @date 2020/5/3
 */
public class MyWebAuthenticationDetail extends WebAuthenticationDetails {
    public String imageCode;
    public String savedImageCode;

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getSavedImageCode() {
        return savedImageCode;
    }

    public void setSavedImageCode(String savedImageCode) {
        this.savedImageCode = savedImageCode;
    }

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public MyWebAuthenticationDetail(HttpServletRequest request) {
        super(request);
        //补充用户提交的验证码和session中保存的验证码
        this.imageCode = request.getParameter("captcha");
        final HttpSession session = request.getSession();
        this.savedImageCode= (String) session.getAttribute("captcha");
        if (!StringUtils.isEmpty(this.savedImageCode)){
            //清除验证码，不管失败成功，客户端应在登录失败时刷新验证码
            session.removeAttribute("captcha");
        }
    }
}
