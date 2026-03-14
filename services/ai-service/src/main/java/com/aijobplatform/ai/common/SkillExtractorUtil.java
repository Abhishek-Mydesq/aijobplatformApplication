package com.aijobplatform.ai.common;
import com.aijobplatform.ai.common.skill.SkillProvider;
import com.aijobplatform.ai.common.skill.StaticSkillProvider;
import java.util.ArrayList;
import java.util.List;

public class SkillExtractorUtil {

    private static final SkillProvider skillProvider =
            new StaticSkillProvider();

    public static List<String> extractSkills(String resumeText) {
        List<String> detectedSkills = new ArrayList<>();
        if (resumeText == null || resumeText.isEmpty()) {
            return detectedSkills;
        }
        List<String> skills = skillProvider.getSkills();
        String lowerText = resumeText.toLowerCase();
        for (String skill : skills) {
            if (lowerText.contains(skill.toLowerCase())) {
                detectedSkills.add(skill);
            }
        }
        return detectedSkills;
    }
}