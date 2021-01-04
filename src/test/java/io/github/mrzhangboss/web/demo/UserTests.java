package io.github.mrzhangboss.web.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserConfigProperties config;

    void testLogin(UserBean user, ResultMatcher expect, MockHttpSession session) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/session").session(session).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(obj2json(user)))
                .andExpect(status().isOk())
                .andExpect(expect);
    }

    void testUser(UserBean user, ResultMatcher content) throws Exception {

        MockHttpSession session = (MockHttpSession)mvc.perform(MockMvcRequestBuilders.get("/api/captcha")).andExpect(status().isOk()).andReturn()
                .getRequest()
                .getSession();
        Assert.notNull(session, "session 不能为空");
        user.setCaptcha("ErrorCaptcha");
        testLogin(user, jsonPath("$.code", is(500)).exists(), session);
        Assert.isNull(session.getAttribute("captcha"), "验证码必须失效");
        session = (MockHttpSession)mvc.perform(MockMvcRequestBuilders.get("/api/captcha")).andExpect(status().isOk()).andReturn()
                .getRequest()
                .getSession();
        user.setCaptcha((String)session.getAttribute("captcha"));
        testLogin(user, content, session);


    }

    String obj2json(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
        return json;
    }


    UserBean getUser(String username, String password) throws Exception {
        UserBean user = new UserBean();
        user.setUser(username);
        user.setPassword(password);
        return user;
    }

    String getInfo(int code) {
        ResBean<String> bean = new ResBean<>();
        bean.setCode(code);
        return obj2json(bean);

    }

    @Test
    void testLoginSession() throws Exception {
        Map<String, String> map = config.getPasswords();
        for (String username: map.keySet()) {
            String password = map.get(username);
            UserBean user = getUser(username, password);
            UserBean errorUser = getUser(username, password + Math.random());
            testUser(errorUser, jsonPath("$.code", is(200)).exists());
            testUser(user, content().string(equalTo(obj2json(ResBean.LoginSuccess()))));
            testUser(errorUser, jsonPath("$.code", is(500)).exists());

        }



    }

}
