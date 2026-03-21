package com.aijobplatform.ai.service.impl;
import com.aijobplatform.ai.client.ResumeServiceClient;
import com.aijobplatform.ai.common.ResumeParserUtil;
import com.aijobplatform.ai.common.ResumeScoreUtil;
import com.aijobplatform.ai.common.SkillExtractorUtil;
import com.aijobplatform.ai.common.status.AnalysisStatus;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.dto.PageResponse;
import com.aijobplatform.ai.dto.ResumeResponse;
import com.aijobplatform.ai.entity.ResumeAnalysis;
import com.aijobplatform.ai.exception.ResourceNotFoundException;
import com.aijobplatform.ai.repository.ResumeAnalysisRepository;
import com.aijobplatform.ai.service.AiResumeService;
import com.aijobplatform.ai.service.kafka.SearchProducer;
import com.aijobplatform.ai.service.kafka.dto.SearchEvent;
import com.aijobplatform.ai.service.llm.LLMService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AiResumeServiceImpl implements AiResumeService {

    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final ResumeServiceClient resumeServiceClient;
    private final LLMService llmService;
    private final SearchProducer searchProducer;
    // ================================
    // helper mapping
    // ================================

    private AiResumeResponse toResponse(ResumeAnalysis entity) {

        AiResumeResponse r = new AiResumeResponse();
        r.setAnalysisId(entity.getId());
        r.setResumeScore(entity.getResumeScore());
        r.setSummary(entity.getSummary());
        r.setStatus(entity.getStatus());
        r.setExperienceYears(entity.getExperienceYears());

        if (entity.getSkills() != null) {
            r.setSkills(List.of(entity.getSkills().split(",")));
        }

        r.setMessage("Analysis fetched");

        return r;
    }

    // ================================
    // parse
    // ================================

    @Override
    public AiResumeResponse parseResume(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResourceNotFoundException("Resume file not provided");
        }

        String resumeText = ResumeParserUtil.extractText(file);

        List<String> skills =
                SkillExtractorUtil.extractSkills(resumeText);

        int score =
                ResumeScoreUtil.calculateScore(
                        skills,
                        resumeText
                );

        ResumeAnalysis entity = new ResumeAnalysis();

        entity.setSkills(String.join(",", skills));
        entity.setResumeScore(score);
        entity.setStatus(AnalysisStatus.DONE.name());

        ResumeAnalysis saved =
                resumeAnalysisRepository.save(entity);

        return toResponse(saved);
    }

    // ================================
    // analyze
    // ================================

    @Override
    public AiResumeResponse analyzeResume(Long resumeId) {

        ResumeAnalysis entity =
                resumeAnalysisRepository
                        .findByResumeId(resumeId)
                        .orElse(null);
        if (entity == null) {
            entity = new ResumeAnalysis();
        }
        entity.setResumeId(resumeId);
        entity.setStatus(AnalysisStatus.PROCESSING.name());

        entity.setResumeId(resumeId);
        entity.setStatus(AnalysisStatus.PROCESSING.name());

        try {

            ResumeResponse resume =
                    resumeServiceClient
                            .getResumeById(resumeId)
                            .getData();

            if (resume == null) {
                throw new ResourceNotFoundException("Resume not found");
            }

            File file = new File(resume.getFilePath());

            if (!file.exists()) {
                throw new ResourceNotFoundException("File not found");
            }

            String resumeText =
                    ResumeParserUtil.extractText(file);

            List<String> skills =
                    SkillExtractorUtil.extractSkills(resumeText);

            String prompt =
                    "You are a resume analyzer.\n" +
                            "Return ONLY in this exact format:\n" +
                            "\n" +
                            "skills: skill1,skill2,skill3\n" +
                            "experience: number\n" +
                            "summary: one line summary\n" +
                            "\n" +
                            "DO NOT write anything else.\n" +
                            "DO NOT add explanation.\n" +
                            "DO NOT change format.\n" +
                            "\nResume:\n" +
                            resumeText;

            String aiResult = null;

            try {
                aiResult = llmService.ask(prompt);
            } catch (Exception e) {
                entity.setStatus(AnalysisStatus.FAILED.name());
            }

            Integer experienceYears = 0;
            String summary = null;

            if (aiResult != null) {

                String lower = aiResult.toLowerCase();

                int skillIdx = lower.indexOf("skills:");

                if (skillIdx != -1) {

                    String part =
                            lower.substring(skillIdx + 7);

                    String[] arr =
                            part.split("\n")[0].split(",");

                    for (String s : arr) {

                        String skill = s.trim();

                        if (!skill.isEmpty()
                                && !skills.contains(skill)) {

                            skills.add(skill);
                        }
                    }
                }

                int expIdx = lower.indexOf("experience");

                if (expIdx != -1) {

                    String part =
                            aiResult
                                    .substring(expIdx)
                                    .split("\n")[0]
                                    .toLowerCase();

                    // extract first number only
                    java.util.regex.Matcher m =
                            java.util.regex.Pattern
                                    .compile("\\d+")
                                    .matcher(part);

                    if (m.find()) {

                        try {
                            experienceYears =
                                    Integer.parseInt(m.group());

                            // limit max experience
                            if (experienceYears > 40) {
                                experienceYears = 5;
                            }

                        } catch (Exception ignored) {
                        }
                    }
                }

                if (experienceYears == 0 && summary != null) {

                    if (summary.toLowerCase().contains("student")
                            || summary.toLowerCase().contains("intern")
                            || summary.toLowerCase().contains("fresher")) {

                        experienceYears = 1;
                    }
                }

                int sumIdx = lower.indexOf("summary");

                if (sumIdx != -1) {

                    summary =
                            aiResult
                                    .substring(sumIdx)
                                    .replace("summary:", "")
                                    .replace("summary", "")
                                    .replace(":", "")
                                    .trim();
                }

                if (sumIdx != -1) {

                    summary =
                            aiResult.substring(sumIdx + 8)
                                    .trim();
                }
            }

            int score =
                    ResumeScoreUtil.calculateScore(
                            skills,
                            resumeText
                    );

            entity.setUserId(resume.getUserId());
            entity.setSkills(String.join(",", skills));
            entity.setResumeScore(score);
            entity.setExperienceYears(experienceYears);
            entity.setSummary(
                    summary != null && !summary.isBlank()
                            ? summary
                            : "Summary not generated"
            );
            entity.setAiResponse(
                    aiResult != null
                            ? aiResult
                            : "AI timeout"
            );

            if (!AnalysisStatus.FAILED.name().equals(entity.getStatus())) {
                entity.setStatus(AnalysisStatus.DONE.name());
            }

            ResumeAnalysis saved =
                    resumeAnalysisRepository.save(entity);
            SearchEvent event =
                    SearchEvent.builder()
                            .refId(saved.getId())
                            .refType("AI")
                            .title("AI Analysis")
                            .content(saved.getSkills())
                            .tags(saved.getStatus())
                            .build();

            searchProducer.send(event);

            return toResponse(saved);

        } catch (Exception ex) {

            entity.setStatus(AnalysisStatus.FAILED.name());

            resumeAnalysisRepository.save(entity);

            throw ex;
        }
    }

    // ================================
    // GET
    // ================================

    @Override
    public AiResumeResponse getByResumeId(Long resumeId) {

        ResumeAnalysis entity =
                resumeAnalysisRepository
                        .findByResumeId(resumeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Not found"));

        return toResponse(entity);
    }

    @Override
    public AiResumeResponse getById(Long id) {

        ResumeAnalysis entity =
                resumeAnalysisRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Not found"));

        return toResponse(entity);
    }

    @Override
    public List<AiResumeResponse> getByUserId(Long userId) {

        return resumeAnalysisRepository
                .findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AiResumeResponse> getAll(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<ResumeAnalysis> result =
                resumeAnalysisRepository.findAll(pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }
    // ================================
    // reanalyze
    // ================================

    @Override
    public AiResumeResponse reanalyze(Long resumeId) {
        return analyzeResume(resumeId);
    }
}