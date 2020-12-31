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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class UserTests {
    @Autowired
    private MockMvc mvc;

    void testUser(String user, ResultMatcher content) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/session").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(user))
                .andExpect(status().isOk())
                .andExpect(content);

    }

    String obj2json(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
        return json;
    }


    String getUser(String password) throws Exception {
        UserBean user = new UserBean();
        user.setUser("admin");
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
        String user = getUser("1234567");
        String errorUser = getUser("12345679");
        testUser(user,  content().string(equalTo(obj2json(ResBean.LoginSuccess()))));
        testUser(errorUser, content().string(equalTo(obj2json(ResBean.LoginError()))));


    }

}
