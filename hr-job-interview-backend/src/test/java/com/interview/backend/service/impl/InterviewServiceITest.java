package com.interview.backend.service.impl;

import com.interview.backend.exception.InterviewBackendException;
import com.interview.backend.model.Candidate;
import com.interview.backend.model.Interview;
import com.interview.backend.repository.InterviewRepository;
import com.interview.backend.service.InterviewService;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class InterviewServiceITest {

    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewRepository interviewRepository;

    @Test
    @DirtiesContext
    void saveInterview_successful_forNewInterview() {
        Interview interview = makeInterview();

        Interview actual = interviewService.save(interview);

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(interview));
    }


    @Test
    @DirtiesContext
    void findAll_successful_forAllInterviews() {
        List<Interview> interviews = new ArrayList<>(makeInterviewList());
        interviews.forEach(i-> interviewService.save(i));
        List<Interview> actual = interviewService.findAll();

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(interviews));
    }


    @Test
    @DirtiesContext
    void findInterviewById_successful_forSpecificInterview() {
        Interview interview = makeInterview();

        interviewRepository.save(interview);
        Interview actual = interviewService.findInterviewById(interview.getId());

        assertNotNull(actual);
        actual.getCandidate().setInterviews(null);
        assertThat(actual, samePropertyValuesAs(interview));
    }


    @Test
    @DirtiesContext
    void searchInterviews_successful_forFindFilteredInterview() {
        Interview i1 = makeInterview();
        i1.getCandidate().setName("Alic");
        i1.getCandidate().setSurname("Defne");
        i1.getCandidate().setEmail("sample@gmail.com");
        i1.getCandidate().setMobile("+909112435678");
        interviewService.save(i1);

        Interview i2 = makeInterview();
        i2.getCandidate().setName("Isla");
        i2.getCandidate().setSurname("Younan");
        i2.getCandidate().setEmail("another@gmail.com");
        i2.getCandidate().setMobile("+438247851694");
        interviewService.save(i2);

        List<Interview> actual = interviewService.searchInterviews("a", "n", "t", "24");

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(new ArrayList<>(List.of(i2))));
    }

    @Test
    @DirtiesContext
    void searchInterviews_successful_notFindAnyInterview() {
        Interview i1 = makeInterview();
        i1.getCandidate().setName("Alic");
        i1.getCandidate().setSurname("Defne");
        i1.getCandidate().setEmail("sample@gmail.com");
        i1.getCandidate().setMobile("+909112435678");
        interviewService.save(i1);

        Interview i2 = makeInterview();
        i2.getCandidate().setName("Isla");
        i2.getCandidate().setSurname("Younan");
        i2.getCandidate().setEmail("another@gmail.com");
        i2.getCandidate().setMobile("+438247851694");
        interviewService.save(i2);

        List<Interview> actual = interviewService.searchInterviews("a", "n", "t", "11");

        assertNotNull(actual);
        assertThat(actual, hasSize(0));
    }

    @Test
    @DirtiesContext
    void deleteInterviewById_successful_forSpecificInterview() {
        Interview interview = makeInterview();
        interviewService.save(interview);

        // load before delete
        Interview actual = interviewService.findInterviewById(interview.getId());
        assertNotNull(actual);
        actual.getCandidate().setInterviews(null);
        assertThat(actual, samePropertyValuesAs(interview));

        interviewRepository.deleteById(interview.getId());

        // load after delete except throws exception
        Exception exception = Assertions.assertThrows(InterviewBackendException.class, () -> {
            interviewService.findInterviewById(interview.getId());
        });

        String expectedMessage = String.format("Interview not found by Id:[%s]", interview.getId());
        String actualMessage = exception.getMessage();
        MatcherAssert.assertThat(actualMessage, Matchers.startsWith(expectedMessage));
    }


    private List<Interview> makeInterviewList() {
        return IntStream.range(1, 4)
                .mapToObj(i -> makeInterview())
                .collect(Collectors.toList());
    }

    private Interview makeInterview() {
        String name = RandomStringUtils.randomAlphanumeric(12);
        return Interview.builder()
                .candidate(Candidate.builder()
                        .name(name)
                        .surname(RandomStringUtils.randomAlphanumeric(12))
                        .email(name.concat("@gmail.com"))
                        .mobile("+".concat(RandomStringUtils.randomNumeric(12)))
                        .build())
                .interviewDatetime(LocalDateTime.now())
                .salaryExpectation(Integer.valueOf(RandomStringUtils.randomNumeric(4, 5)))
                .commentForCandidate(RandomStringUtils.randomPrint(50))
                .build();
    }


}



