package io.github.mrzhangboss.web.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "user")
public class UserConfigProperties {
    private Map<String, String> passwords = new HashMap<>();

    public Map<String, String> getPasswords() {
        return passwords;
    }

    public void setPasswords(Map<String, String> passwords) {
        this.passwords = passwords;
    }
}
