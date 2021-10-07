package com.interview.backend.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;
import java.util.List;

/**
 * This class holds a list of {@code ErrorModel} that describe the error raised on rejected validation
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    Date timestamp;
    int status;
    String message;
    List<ErrorModel> errors;
    String path;
}