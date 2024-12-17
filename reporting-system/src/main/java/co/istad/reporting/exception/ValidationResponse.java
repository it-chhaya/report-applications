package co.istad.reporting.exception;

import lombok.Builder;

@Builder
public record ValidationResponse(
        String field,
        String description
) {
}
