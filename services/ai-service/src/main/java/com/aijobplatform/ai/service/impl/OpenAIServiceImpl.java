package com.aijobplatform.ai.service.impl;
import com.aijobplatform.ai.config.OpenAIConfig;
import com.aijobplatform.ai.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {
    private final OpenAIConfig config;
    private final WebClient webClient = WebClient.builder().build();


    @Override
    public String askAI(String prompt) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "messages", new Object[]{
                        Map.of("role", "user", "content", prompt)
                }
        );

        Map response = webClient.post()
                .uri(config.getApiUrl())
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + config.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) {
            return null;
        }

        var choices = (java.util.List<Map>) response.get("choices");

        var msg = (Map) choices.get(0).get("message");

        return msg.get("content").toString();
    }
}