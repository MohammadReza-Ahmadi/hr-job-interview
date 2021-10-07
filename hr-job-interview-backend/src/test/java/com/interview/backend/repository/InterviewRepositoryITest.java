package com.interview.backend.repository;

import com.interview.backend.model.Candidate;
import com.interview.backend.model.Interview;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class InterviewRepositoryITest {

    @Autowired
    private InterviewRepository interviewRepository;

    @Test
    @DirtiesContext
    void saveInterview_successful_forNewInterview() {
        Interview interview = makeInterview();

        Interview actual = interviewRepository.save(interview);

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(interview));
    }


    @Test
    @DirtiesContext
    void findInterviewById_successful_forSpecificInterview() {
        Interview interview = makeInterview();

        interviewRepository.save(interview);
        Optional<Interview> actual = interviewRepository.findById(interview.getId());

        assertNotNull(actual);
        assertTrue(actual.isPresent());
        actual.get().getCandidate().setInterviews(null);
        assertThat(actual.get(), samePropertyValuesAs(interview));
    }

    @Test
    @DirtiesContext
    void findAll_successful_forAllInterviews() {
        List<Interview> interviews = new ArrayList<>(makeInterviewList());
        interviewRepository.saveAll(interviews);
        List<Interview> actual = interviewRepository.findAll();

        assertNotNull(actual);
        assertThat(actual, samePropertyValuesAs(interviews));
    }


    @Test
    @DirtiesContext
    void searchInterviews_successful_forFindFilteredInterview() {
        Interview i1 = makeInterview();
        i1.getCandidate().setName("Alic");
        i1.getCandidate().setSurname("Defne");
        i1.getCandidate().setEmail("sample@gmail.com");
        i1.getCandidate().setMobile("+909112435678");
        interviewRepository.save(i1);

        Interview i2 = makeInterview();
        i2.getCandidate().setName("Isla");
        i2.getCandidate().setSurname("Younan");
        i2.getCandidate().setEmail("another@gmail.com");
        i2.getCandidate().setMobile("+438247851694");
        interviewRepository.save(i2);

        List<Interview> actual = interviewRepository.searchInterviews("a", "n", "t", "24");

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
        interviewRepository.save(i1);

        Interview i2 = makeInterview();
        i2.getCandidate().setName("Isla");
        i2.getCandidate().setSurname("Younan");
        i2.getCandidate().setEmail("another@gmail.com");
        i2.getCandidate().setMobile("+438247851694");
        interviewRepository.save(i2);

        List<Interview> actual = interviewRepository.searchInterviews("a", "n", "t", "11");

        assertNotNull(actual);
        assertThat(actual, hasSize(0));
    }

    @Test
    void deleteInterviewById_successful_forSpecificInterview() {
        Interview interview = makeInterview();
        interviewRepository.save(interview);

        // load before delete
        Optional<Interview> actual = interviewRepository.findById(interview.getId());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        actual.get().getCandidate().setInterviews(null);
        assertThat(actual.get(), samePropertyValuesAs(interview));

        interviewRepository.deleteById(interview.getId());

        // load after delete
        actual = interviewRepository.findById(interview.getId());
        assertFalse(actual.isPresent());
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