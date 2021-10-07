package com.interview.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.backend.controller.dto.InterviewDto;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class InterviewControllerITest extends BaseIntegrationTest {
    private static final String INTERVIEWS_ENDPOINT = "/interviews";

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DirtiesContext
    void creatInterview_successful_forNewInterview() throws Exception {
        InterviewDto interviewDto = makeInterviewDto();

        // create interview
        mockMvc.perform(MockMvcRequestBuilders.post(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        // load interview
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(INTERVIEWS_ENDPOINT.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        InterviewDto actual = readInterviewDto(mvcResult);
        interviewDto.setId(actual.getId());
        assertThat(actual, samePropertyValuesAs(interviewDto));
    }

    @Test
    @DirtiesContext
    void getInterviews_successful_forAllInterviews() throws Exception {
        List<InterviewDto> interviewDtoList = new ArrayList<>();

        // create some interviews
        IntStream.range(1, 6).forEach((i -> {
            try {
                // create interview
                InterviewDto interviewDto = makeInterviewDto();
                interviewDtoList.add(interviewDto);
                mockMvc.perform(MockMvcRequestBuilders.post(INTERVIEWS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interviewDto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // get all interviews
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5)))
                .andReturn();

        List<InterviewDto> actual = readInterviewDtoList(mvcResult);
        assertThat(actual, Matchers.containsInAnyOrder(interviewDtoList.toArray()));
    }

    @Test
    @DirtiesContext
    void getInterviewById_successful_forSpecificInterview() throws Exception {
        InterviewDto interviewDto = makeInterviewDto();

        // create interview
        mockMvc.perform(MockMvcRequestBuilders.post(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        // get saved interview
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andReturn();

        InterviewDto actual = readInterviewDtoList(mvcResult).get(0);
        assertThat(actual, samePropertyValuesAs(interviewDto));
    }

    @Test
    @DirtiesContext
    void searchInterviews_successful_forFindFilteredInterview() throws Exception {
        InterviewDto interviewDto1 = makeInterviewDto();
        interviewDto1.setName("Alic");
        interviewDto1.setSurname("Defne");
        interviewDto1.setEmail("sample@gmail.com");
        interviewDto1.setMobile("+909112435678");

        // save first interview
        mockMvc.perform(MockMvcRequestBuilders.post(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewDto1)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        InterviewDto interviewDto2 = makeInterviewDto();
        interviewDto2.setName("Isla");
        interviewDto2.setSurname("Younan");
        interviewDto2.setEmail("another@gmail.com");
        interviewDto2.setMobile("+438247851694");

        // save second interview
        mockMvc.perform(MockMvcRequestBuilders.post(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewDto2)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();


        // search interview
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(INTERVIEWS_ENDPOINT + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("name", "a")
                .queryParam("surname", "n")
                .queryParam("email", "t")
                .queryParam("mobile", "24"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andReturn();

        List<InterviewDto> actual = readInterviewDtoList(mvcResult);
        assertThat(actual, samePropertyValuesAs(new ArrayList<>(List.of(interviewDto2))));
    }

    @Test
    @DirtiesContext
    void deleteInterviewById_successful_forSpecificInterview() throws Exception {
        InterviewDto interviewDto = makeInterviewDto();

        // save first interview
        mockMvc.perform(MockMvcRequestBuilders.post(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        // load before delete
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andReturn();

        InterviewDto actual = readInterviewDtoList(mvcResult).get(0);
        assertThat(actual, samePropertyValuesAs(interviewDto));
        String id = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$[0].id").toString();

        // delete interview
        mockMvc.perform(MockMvcRequestBuilders.delete(INTERVIEWS_ENDPOINT.concat("/").concat(id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        // load after delete
        mockMvc.perform(MockMvcRequestBuilders.get(INTERVIEWS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)))
                .andReturn();
    }

    private InterviewDto makeInterviewDto() {
        String name = RandomStringUtils.randomAlphanumeric(12);
        return InterviewDto.builder()
                .name(name)
                .surname(RandomStringUtils.randomAlphanumeric(12))
                .email(name.concat("@gmail.com"))
                .mobile("+".concat(RandomStringUtils.randomNumeric(12)))
                .salaryExpectation(Integer.valueOf(RandomStringUtils.randomNumeric(4, 5)))
                .commentForCandidate(RandomStringUtils.randomPrint(50))
                .interviewDateTime(LocalDateTime.now())
                .build();
    }

    private InterviewDto readInterviewDto(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String json = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(json, InterviewDto.class);
    }

    private List<InterviewDto> readInterviewDtoList(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String json = mvcResult.getResponse().getContentAsString();
        InterviewDto[] interviewDtoArr = objectMapper.readValue(json, InterviewDto[].class);
        return Arrays.stream(interviewDtoArr)
                .map(i -> InterviewDto.builder()
                        .name(i.getName())
                        .surname(i.getSurname())
                        .email(i.getEmail())
                        .mobile(i.getMobile())
                        .salaryExpectation(i.getSalaryExpectation())
                        .commentForCandidate(i.getCommentForCandidate())
                        .interviewDateTime(i.getInterviewDateTime())
                        .build())
                .collect(Collectors.toList());
    }
}