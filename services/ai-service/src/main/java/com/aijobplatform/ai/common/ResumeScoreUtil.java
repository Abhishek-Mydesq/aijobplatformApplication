package com.aijobplatform.ai.common;

import java.util.List;

public class ResumeScoreUtil {

    private ResumeScoreUtil() {}

    public static int calculateScore(List<String> skills, String text) {

        int score = 0;
        score += skills.size() * 5;
        score += importantSkillBonus(skills);
        score += experienceScore(text);
        score += educationScore(text);
        score += keywordBonus(text);
        if (score > 100) {
            score = 100;
        }

        return score;
    }


    private static int importantSkillBonus(List<String> skills) {

        int bonus = 0;

        for (String s : skills) {

            if (s.equalsIgnoreCase("java")
                    || s.equalsIgnoreCase("spring")
                    || s.equalsIgnoreCase("microservices")
                    || s.equalsIgnoreCase("docker")
                    || s.equalsIgnoreCase("kubernetes")
                    || s.equalsIgnoreCase("aws")) {

                bonus += 3;
            }
        }

        return bonus;
    }
    private static int experienceScore(String text) {

        String t = text.toLowerCase();

        if (t.contains("5 years")) return 20;
        if (t.contains("4 years")) return 16;
        if (t.contains("3 years")) return 12;
        if (t.contains("2 years")) return 8;
        if (t.contains("1 year")) return 5;
        return 0;
    }

    private static int educationScore(String text) {

        String t = text.toLowerCase();

        if (t.contains("b.tech")) return 5;
        if (t.contains("m.tech")) return 8;
        if (t.contains("bachelor")) return 5;
        if (t.contains("master")) return 8;
        if (t.contains("phd")) return 10;
        return 0;
    }


    private static int keywordBonus(String text) {
        String t = text.toLowerCase();
        int bonus = 0;
        if (t.contains("microservices")) bonus += 5;
        if (t.contains("cloud")) bonus += 5;
        if (t.contains("rest api")) bonus += 3;
        if (t.contains("spring boot")) bonus += 4;

        return bonus;
    }
}