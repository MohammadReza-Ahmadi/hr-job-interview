package com.interview.backend.service.impl;

import com.interview.backend.model.Candidate;
import com.interview.backend.model.Interview;
import com.interview.backend.repository.InterviewRepository;
import com.interview.backend.service.InterviewService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class InterviewServiceUTest {

    @Autowired
    private InterviewService interviewService;
    @MockBean
    private InterviewRepository interviewRepository;


    @Test
    void saveInterview_successful_forNewInterview() {
        Interview interview = makeInterview(1L);
        when(interviewRepository.save(interview)).thenReturn(interview);
        Interview actual = interviewService.save(interview);

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(interview));
    }

    @Test
    void findAll_successful_forAllInterviews() {
        List<Interview> interviews = makeInterviewList();
        when(interviewRepository.findAll()).thenReturn(interviews);
        List<Interview> actual = interviewService.findAll();

        assertNotNull(actual);
        assertThat(actual, Matchers.containsInAnyOrder(interviews.toArray()));
    }

    @Test
    void findInterviewById_successful_forSpecificInterview() {
        Long id = 1L;
        Interview interview = makeInterview(id);
        when(interviewRepository.findById(id)).thenReturn(Optional.of(interview));
        Interview actual = interviewService.findInterviewById(id);

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(interview));
    }

    @Test
    void searchInterviews_successful_forFilterValues() {
        Interview interview = makeInterview(1L);
        String name = interview.getCandidate().getName();
        String surname = interview.getCandidate().getSurname();
        String email = interview.getCandidate().getEmail();
        String mobile = interview.getCandidate().getMobile();
        when(interviewRepository.searchInterviews(name, surname, email, mobile)).thenReturn(List.of(interview));
        List<Interview> actual = interviewService.searchInterviews(name, surname, email, mobile);

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(new ArrayList<>(List.of(interview))));
    }

    @Test
    void deleteInterviewById_successful_forSpecificInterview() {
        Long id = 1L;
        doNothing().when(interviewRepository).deleteById(id);
        interviewService.deleteInterviewById(id);
        verify(interviewRepository).deleteById(id);
    }

    private List<Interview> makeInterviewList() {
        return IntStream.range(1, 8)
                .mapToObj(i -> makeInterview((long) i))
                .collect(Collectors.toList());
    }

    private Interview makeInterview(Long id) {
        String name = RandomStringUtils.randomAlphanumeric(12);
        return Interview.builder()
                .candidate(Candidate.builder()
                        .name(name)
                        .surname(RandomStringUtils.randomAlphanumeric(12))
                        .email(name.concat("@gmail.com"))
                        .mobile("+".concat(RandomStringUtils.randomNumeric(12)))
                        .id(id)
                        .build())
                .interviewDatetime(LocalDateTime.now())
                .salaryExpectation(Integer.valueOf(RandomStringUtils.randomNumeric(4, 5)))
                .commentForCandidate(RandomStringUtils.randomPrint(50))
                .id(id)
                .build();
    }

}



