package com.aijobplatform.ai.common;

import java.util.*;

public class SkillExtractorUtil {

    private static final List<String> SKILLS = Arrays.asList(
            "java",
            "spring",
            "spring boot",
            "microservices",
            "docker",
            "kubernetes",
            "aws",
            "mysql",
            "postgresql",
            "mongodb",
            "react",
            "angular",
            "javascript",
            "python",
            "git"
    );

    public static List<String> extractSkills(String resumeText) {

        List<String> detectedSkills = new ArrayList<>();

        if (resumeText == null || resumeText.isEmpty()) {
            return detectedSkills;
        }

        String lowerText = resumeText.toLowerCase();

        for (String skill : SKILLS) {

            if (lowerText.contains(skill)) {
                detectedSkills.add(skill);
            }
        }

        return detectedSkills;
    }
}