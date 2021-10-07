package com.interview.frontend.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeignExceptionMessage {
    Date timestamp;
    int status;
    String message;
    List<ErrorModel> errors;
    String path;
}
