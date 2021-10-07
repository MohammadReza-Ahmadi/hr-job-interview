package com.interview.backend.service.impl;

import com.interview.backend.exception.InterviewBackendException;
import com.interview.backend.model.Interview;
import com.interview.backend.repository.InterviewRepository;
import com.interview.backend.service.InterviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;


    @Override
    public Interview save(Interview interview) {
        interview = interviewRepository.save(interview);
        log.info("interview:[{}] is saved.", interview);
        return interview;
    }

    @Override
    public List<Interview> findAll() {
        var it = interviewRepository.findAll();
        log.info("all interviews fetched from db.");
        return new ArrayList<>(it);
    }

    @Override
    public Interview findInterviewById(Long id) {
        return interviewRepository.findById(id).orElseThrow(() -> new InterviewBackendException(String.format("Interview not found by Id:[%s]", id)));
    }

    @Override
    public List<Interview> searchInterviews(String name, String surname, String email, String mobile) {
        var it = interviewRepository.searchInterviews(name, surname, email, mobile);
        log.info("interviews are fetched by containing properties:[name={}, surname={}, email={}, mobile={}]", name, surname, email, mobile);
        return new ArrayList<>(it);
    }

    @Override
    public void deleteInterviewById(Long id) {
        try {
            interviewRepository.deleteById(id);
        } catch (Exception e) {
            if (e instanceof EmptyResultDataAccessException){
                throw new InterviewBackendException(String.format("Interview not found by Id:[%s]", id));
            } else {
                e.printStackTrace();
            }
        }
        log.info("interview by id:[{}] is removed.", id);
    }
}
