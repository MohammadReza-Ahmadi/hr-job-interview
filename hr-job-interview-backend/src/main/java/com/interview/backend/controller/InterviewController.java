package com.interview.backend.controller;

import com.interview.backend.controller.dto.InterviewDto;
import com.interview.backend.controller.mapper.InterviewMapper;
import com.interview.backend.model.Interview;
import com.interview.backend.service.InterviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/interviews")
@AllArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;
    private final InterviewMapper mapper = new InterviewMapper();

    @PostMapping
    public ResponseEntity<Void> creatInterview(@Valid @RequestBody InterviewDto request) {
        log.info("creating interview data ...");
        Interview interview = mapper.convertInterviewDtoToInterview(request);
        interviewService.save(interview);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InterviewDto>> getInterviews() {
        log.info("loading interviews ...");
        List<Interview> interviews = interviewService.findAll();
        return new ResponseEntity<>(mapper.convertInterviewListToInterviewResponseList(interviews), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<InterviewDto> getInterviewById(@PathVariable Long id) {
        log.info("loading interview by id:[{}] ...", id);
        Interview interview = interviewService.findInterviewById(id);
        return new ResponseEntity<>(mapper.convertInterviewToInterviewDto(interview), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<InterviewDto>> searchInterviews(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String surname,
                                                               @RequestParam(required = false) String email,
                                                               @RequestParam(required = false) String mobile) {
        log.info("searching interviews by parameters:[name={}, surname={}, email={}, mobile={}]", name, surname, email, mobile);
        List<Interview> interviews = interviewService.searchInterviews(name, surname, email, mobile);
        return new ResponseEntity<>(mapper.convertInterviewListToInterviewResponseList(interviews), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewById(@PathVariable Long id) {
        log.info("deleting interview by id:[{}] ...", id);
        interviewService.deleteInterviewById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
