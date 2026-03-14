package com.aijobplatform.ai.controller;

import com.aijobplatform.ai.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OllamaTestController {

    private final LLMService llmService;

    @GetMapping("/ai/test")
    public String testAI() {

        String prompt = "Say hello from AI";

        return llmService.ask(prompt);
    }
}