package io.github.mrzhangboss.web.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserConfigProperties userConfig;

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResBean<String> login(@RequestBody UserBean user) {
        Map<String, String> passwords = userConfig.getPasswords();
        String username = user.getUser();
        String password = user.getPassword();
        if (passwords.containsKey(username))
            if (passwords.get(username).equals(password))
                return ResBean.LoginSuccess();
        return ResBean.LoginError();

    }
}
