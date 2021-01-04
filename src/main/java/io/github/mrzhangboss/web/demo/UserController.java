package io.github.mrzhangboss.web.demo;

import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserConfigProperties userConfig;

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResBean<String> login(HttpServletRequest request, @RequestBody UserBean user) {
        if (!CaptchaUtil.ver(user.getCaptcha(), request)) {
            return ResBean.LoginCaptchaError();
        }
        Map<String, String> passwords = userConfig.getPasswords();
        String username = user.getUser();
        String password = user.getPassword();
        if (passwords.containsKey(username))
            if (passwords.get(username).equals(password))
                return ResBean.LoginSuccess();
        return ResBean.LoginError();
    }
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.out(request, response);
    }
}
