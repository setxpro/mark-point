package br.com.bytestorm.mark_point.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"httpStatus", "status", "message", "msgServer", "messages", "timeStamp"})
public class StandarError implements Serializable {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    private String message;
    private String msgServer;
    private Long timeStamp;
    private List<String> messages;
}
