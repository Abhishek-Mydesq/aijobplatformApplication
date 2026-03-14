package com.aijobplatform.ai.service.llm.impl;

import com.aijobplatform.ai.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OllamaServiceImpl implements LLMService {

    private final WebClient.Builder webClientBuilder;

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @Override
    public String ask(String prompt) {

        Map<String, Object> body = Map.of(
                "model", "phi3",
                "prompt", prompt,
                "stream", false
        );

        Map response =
                webClientBuilder
                        .build()
                        .post()
                        .uri(OLLAMA_URL)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .timeout(java.time.Duration.ofSeconds(30))
                        .block();

        if (response == null) {
            return null;
        }

        Object result = response.get("response");

        return result != null ? result.toString() : null;
    }
}