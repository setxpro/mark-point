package br.com.bytestorm.mark_point.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage extends StandarError {
    private static final long serialVersionUID = 1L;

    private HttpStatusResponse httpStatus;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HttpStatusResponse implements Serializable {
        private int code;
        private String message;
    }
}
