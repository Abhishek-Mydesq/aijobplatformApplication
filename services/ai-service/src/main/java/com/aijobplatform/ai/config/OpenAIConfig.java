package com.aijobplatform.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class OpenAIConfig {

    private String apiKey;
    @Value("${openai.api.url}")
    private String apiUrl;

}