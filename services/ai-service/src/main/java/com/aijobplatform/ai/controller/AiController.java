package com.aijobplatform.ai.controller;

import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.service.AiResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.aijobplatform.ai.dto.PageResponse;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiResumeService aiResumeService;
    // OLD API
    /*
    @PostMapping("/parse-resume")
    public ResponseEntity<APIResponse<AiResumeResponse>> parseResume(
            @RequestParam("file") MultipartFile file) {

        AiResumeResponse response = aiResumeService.parseResume(file);

        return ResponseEntity.ok(
                new APIResponse<>(true, "Resume analyzed successfully", response)
        );
    }
    */


    //NEW API
    @PostMapping("/analyze/{resumeId}")
    public ResponseEntity<APIResponse<AiResumeResponse>> analyzeResume(
            @PathVariable Long resumeId
    ) {
        AiResumeResponse response = aiResumeService.analyzeResume(resumeId);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Resume analyzed successfully", response)
        );
    }
    // GET by analysis id
    @GetMapping("/analysis/{id}")
    public ResponseEntity<APIResponse<AiResumeResponse>> getById(
            @PathVariable Long id
    ) {

        AiResumeResponse response =
                aiResumeService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Analysis fetched",
                        response
                )
        );
    }


    // GET by resumeId
    @GetMapping("/analysis/resume/{resumeId}")
    public ResponseEntity<APIResponse<AiResumeResponse>> getByResumeId(
            @PathVariable Long resumeId
    ) {

        AiResumeResponse response =
                aiResumeService.getByResumeId(resumeId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Analysis fetched",
                        response
                )
        );
    }


    // GET by userId
    @GetMapping("/analysis/user/{userId}")
    public ResponseEntity<APIResponse<List<AiResumeResponse>>> getByUserId(
            @PathVariable Long userId
    ) {

        List<AiResumeResponse> response =
                aiResumeService.getByUserId(userId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Analysis fetched",
                        response
                )
        );
    }


    // PAGINATION
    @GetMapping("/analysis")
    public ResponseEntity<
            APIResponse<PageResponse<AiResumeResponse>>
            > getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        PageResponse<AiResumeResponse> response =
                aiResumeService.getAll(page, size);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Analysis list",
                        response
                )
        );
    }


    //REANALYZE
    @PostMapping("/reanalyze/{resumeId}")
    public ResponseEntity<APIResponse<AiResumeResponse>> reanalyze(
            @PathVariable Long resumeId
    ) {

        AiResumeResponse response =
                aiResumeService.reanalyze(resumeId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Reanalyzed",
                        response
                )
        );
    }

}