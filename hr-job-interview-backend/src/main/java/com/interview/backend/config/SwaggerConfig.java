package com.interview.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

/**
 * Config class for providing swagger documentations.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.interview.backend.controller"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, Arrays.asList(
                        new ResponseBuilder().code("500").description("500 message").build(),
                        new ResponseBuilder().code("403").description("Forbidden!!").build())
                );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("HR Job Interview Backend API")
                .description("Java application to provide a store for HR job interview data")
                .contact(new Contact("MohammadReza Ahmadi", "", "m.reza79ahmadi@gmail.com"))
                .version("1.0").build();
    }
}
