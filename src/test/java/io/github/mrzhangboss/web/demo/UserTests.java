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

import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserConfigProperties config;

    void testUser(String user, ResultMatcher content) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/session").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(user))
                .andExpect(status().isOk())
                .andExpect(content);

    }

    String obj2json(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
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


    String getUser(String username, String password) throws Exception {
        UserBean user = new UserBean();
        user.setUser(username);
        user.setPassword(password);
        return obj2json(user);
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
            String user = getUser(username, password);
            String errorUser = getUser(username, password + Math.random());
            testUser(errorUser, jsonPath("$.code", is(200)).exists());
            testUser(user, content().string(equalTo(obj2json(ResBean.LoginSuccess()))));
            testUser(errorUser, jsonPath("$.code", is(500)).exists());

        }



    }

}
