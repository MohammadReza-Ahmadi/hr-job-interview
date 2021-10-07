package com.interview.backend.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class describe the error object model, which is a simple POJO that contains the rejected filedName, rejectedValue
 * and a messageError.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorModel{
    String fieldName;
    Object rejectedValue;
    String messageError;
}
