package com.interview.backend.controller.mapper;

import com.interview.backend.controller.dto.InterviewDto;
import com.interview.backend.model.Candidate;
import com.interview.backend.model.Interview;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InterviewMapper {

    public List<InterviewDto> convertInterviewListToInterviewResponseList(List<Interview> interviews) {
        log.info("converting interview list to interviewResponse list ...");
        ModelMapper modelMapper = new ModelMapper();
        List<InterviewDto> responses = new ArrayList<>();
        for (Interview interview : interviews) {
            InterviewDto interviewDto = new InterviewDto();
            modelMapper.map(interview, interviewDto);
            modelMapper.map(interview.getCandidate(), interviewDto);
            responses.add(interviewDto);
        }
        return responses;
    }

    public Interview convertInterviewDtoToInterview(InterviewDto interviewDto) {
        ModelMapper modelMapper = new ModelMapper();
        Interview interview = Interview.builder().candidate(new Candidate()).build();
        modelMapper.map(interviewDto, interview);
        modelMapper.map(interviewDto, interview.getCandidate());
        return interview;
    }

    public InterviewDto convertInterviewToInterviewDto(Interview interview) {
        ModelMapper modelMapper = new ModelMapper();
        InterviewDto interviewDto = new InterviewDto();
        modelMapper.map(interview, interviewDto);
        modelMapper.map(interview.getCandidate(), interviewDto);
        return interviewDto;
    }
}
