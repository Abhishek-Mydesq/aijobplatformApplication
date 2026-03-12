package com.aijobplatform.ai.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResumeScoreUtil {

    private static final Map<String, Integer> SKILL_SCORE = new HashMap<>();
    static {

        SKILL_SCORE.put("java", 15);
        SKILL_SCORE.put("spring", 10);
        SKILL_SCORE.put("spring boot", 15);
        SKILL_SCORE.put("microservices", 20);
        SKILL_SCORE.put("docker", 10);
        SKILL_SCORE.put("kubernetes", 10);
        SKILL_SCORE.put("aws", 20);
        SKILL_SCORE.put("mysql", 10);
        SKILL_SCORE.put("react", 10);
        SKILL_SCORE.put("python", 10);

    }

    public static int calculateScore(List<String> skills) {
        int score = 0;
        for (String skill : skills) {
            score += SKILL_SCORE.getOrDefault(skill, 0);
        }
        return score;
    }
}