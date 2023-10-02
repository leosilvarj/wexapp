package br.com.wexapplication.wexapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource(locations = "classpath:application-test.yml")
public class TestConfig {

    @Value("${exchange.api.url}")
    private String exchangeApiUrl;

    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public String exchangeApiUrl() {
        return exchangeApiUrl;
    }
}

