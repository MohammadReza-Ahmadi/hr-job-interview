package com.interview.frontend.repository;

import com.interview.frontend.model.InterviewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "interviewRepository", url = "${backend.url}"
//        configuration = FeignClientConfig.class,
//        fallback = PostFeignClientFallback.class
)
public interface InterviewRepository {

    @GetMapping
    ResponseEntity<List<InterviewDto>> getInterviews();

    @PostMapping
    ResponseEntity<Void> createInterview(@RequestBody InterviewDto request);

    @GetMapping("/{id}")
    ResponseEntity<InterviewDto> getInterviewById(@PathVariable Long id);

    @GetMapping("/")
    ResponseEntity<List<InterviewDto>> searchInterviews(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String surname,
                                                        @RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String mobile);
    @DeleteMapping("/{id}")
    ResponseEntity<Void>  deleteInterviewById(@PathVariable Long id);
}
