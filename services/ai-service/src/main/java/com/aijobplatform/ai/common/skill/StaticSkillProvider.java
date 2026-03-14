package com.aijobplatform.ai.common.skill;
import java.util.List;
public class StaticSkillProvider implements SkillProvider {

    @Override
    public List<String> getSkills() {

        return List.of(
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
                "git",
                "node",
                "nodejs",
                "express",
                "go",
                "rust",
                "c++",
                "c#",
                ".net"
        );
    }
}