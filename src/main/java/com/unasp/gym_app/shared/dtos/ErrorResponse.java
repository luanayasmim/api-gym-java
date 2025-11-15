package com.unasp.gym_app.shared.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private final OffsetDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<FieldErrorDetail> fieldErrors;

    @Getter
    @Builder
    public static class FieldErrorDetail {
        private final String field;
        private final String message;
    }
}

