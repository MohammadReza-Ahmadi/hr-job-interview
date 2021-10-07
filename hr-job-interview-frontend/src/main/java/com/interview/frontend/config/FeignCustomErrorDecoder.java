package com.interview.frontend.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.frontend.exception.FeignExceptionMessage;
import com.interview.frontend.exception.InterviewFrontendException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignCustomErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {

        //START DECODING ORIGINAL ERROR MESSAGE
        String errorMessage = null;
        Reader reader = null;

        //capturing error message from response body.
        try {
            reader = response.body().asReader(StandardCharsets.UTF_8);
            String result = IOUtils.toString(reader);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            FeignExceptionMessage exceptionMessage = mapper.readValue(result,
                    FeignExceptionMessage.class);

            errorMessage = exceptionMessage.getMessage();

        } catch (IOException e) {
            log.error("IO Exception on reading exception message feign client" + e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("IO Exception on reading exception message feign client" + e);
            }
        }

        //END DECODING ORIGINAL ERROR MESSAGE
        switch (response.status()) {
            case 400:
                errorMessage = errorMessage != null ? errorMessage : "Bad Request Through Feign";
                log.error("Error in request went through feign client {} ", errorMessage);
            case 401:
                errorMessage = errorMessage != null ? errorMessage : "Unauthorized Request Through Feign";
                log.error("Error in request went through feign client {} ", errorMessage);
            case 404:
                errorMessage = errorMessage != null ? errorMessage : "Unidentified Request Through Feign";
                log.error("Error in request went through feign client {} ", errorMessage);
            default:
                errorMessage = errorMessage != null ? errorMessage : "Common Feign Exception";
                log.error("Error in request went through feign client {} ", errorMessage);
        }

        return new InterviewFrontendException(errorMessage);
    }

}
