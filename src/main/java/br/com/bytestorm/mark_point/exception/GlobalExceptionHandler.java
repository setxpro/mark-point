package br.com.bytestorm.mark_point.exception;

import br.com.bytestorm.mark_point.entity.dto.ApiErrosDTO;
import br.com.bytestorm.mark_point.enums.EnumException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<StandarError> trataErros(ServiceException ex, Exception e) {
        final ErrorMessage errorMessage = buildErrorMessage(e, ex.getEnumException());
        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<StandarError> trataNegocio(NegocioException ex) {
        final ErrorMessage errorMessage = buildErrorMessage(ex, ex.getEnumException());
        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<StandarError> trataUsernameNotFound(UsernameNotFoundException ex) {
        final ErrorMessage errorMessage = buildErrorMessage(ex, EnumException.UNAUTHORIZED_401);
        return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
    }

    private ErrorMessage buildErrorMessage(final Exception e, final EnumException enumException) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorMessage errorMessage = ErrorMessage.builder()
                .httpStatus(ErrorMessage.HttpStatusResponse.builder()
                        .code(status.value())
                        .message(status.getReasonPhrase())
                        .build())
                .status(status)
                .message(e.getMessage())
                .msgServer(e.getCause() != null ? e.getCause().toString() : null)
                .timeStamp(System.currentTimeMillis())
                .build();

        StackTraceElement[] stackTraceElement = e.getStackTrace();
        List<String> classStackTrace = new ArrayList<>();
        if (stackTraceElement.length > 0) {
            classStackTrace.add(String.format("Classe -> %s", stackTraceElement[0].getClassName()));
            classStackTrace.add(String.format("Linha -> %s", stackTraceElement[0].getLineNumber()));
            classStackTrace.add(String.format("Método -> %s", stackTraceElement[0].getMethodName()));
        }
        String msgCause = "";

        if (enumException.equals(EnumException.NOT_FOUND_404) || (e instanceof ObjectNotFoundException)) {
            status = HttpStatus.NOT_FOUND;
            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }
        if (enumException.equals(EnumException.CONFLICT_409)) {
            status = HttpStatus.CONFLICT;
            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }

        if (enumException.equals(EnumException.FORBIDDEN_403)) {
            status = HttpStatus.FORBIDDEN;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }

        if (enumException.equals(EnumException.UNAUTHORIZED_401)) {
            status = HttpStatus.UNAUTHORIZED;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }

        if (enumException.equals(EnumException.BAD_REQUEST_400)) {
            status = HttpStatus.BAD_REQUEST;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }

        if (e.getCause() instanceof HttpClientErrorException) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
            errorMessage.setMessage(((HttpClientErrorException) e.getCause()).getResponseBodyAsString());
            msgCause = errorMessage.getMessage();
        }
        if (e.getCause() instanceof HttpServerErrorException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
            errorMessage.setMessage(((HttpServerErrorException) e.getCause()).getResponseBodyAsString());
            msgCause = errorMessage.getMessage();
        }
        if (e.getCause() instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }

        if (e.getCause() instanceof ArithmeticException) {
            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }
        if (e.getCause() instanceof ResourceAccessException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
        }
        if (e.getCause() instanceof InvalidFormatException) {
            final InvalidFormatException exception = (InvalidFormatException) e.getCause();
            StringBuilder path = new StringBuilder();
            exception.getPath().forEach(p -> path.append(p.getPropertyName()).append(" -> "));
            path.setLength(path.length() - 4);
            final String msgError = String.format("O atributo %s recebeu um valor do tipo [%s] e é esperado um valor do tipo [%s].",
                    path, exception.getValue().toString(), exception.getTargetType().getName());

            log.error(msgError);

            errorMessage.setMessage(msgError);
            msgCause = errorMessage.getMessage();
        }
        if (e.getCause() instanceof SQLException) {
            final SQLException exception = (SQLException) e.getCause();
            List<String> errors = new ArrayList<>();
            errors.add(String.format("SQLState -> %s", exception.getSQLState()));
            errors.add(String.format("SQLMessage -> %s", exception.getMessage()));

            log.error("Erro ao executar SQL na base de dados. (SQLException) {}", errors);

            status = HttpStatus.INTERNAL_SERVER_ERROR;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
            errorMessage.setMessage(String.format("Erro ao executar SQL na base de dados. (SQLException) %s", errors));
            msgCause = errorMessage.getMessage();
        }
        if (e.getCause() instanceof SQLGrammarException) {
            final SQLGrammarException exception = (SQLGrammarException) e.getCause();
            List<String> errors = new ArrayList<>();
            errors.add(String.format("SQL -> %s", exception.getSQL()));
            errors.add(String.format("SQL Exception -> %s", exception.getSQLException().getMessage()));

            log.error("Erro ao executar SQL na base de dados. (SQLGrammarException) {}", errors);

            status = HttpStatus.INTERNAL_SERVER_ERROR;

            errorMessage.setHttpStatus(ErrorMessage.HttpStatusResponse.builder()
                    .code(status.value())
                    .message(status.getReasonPhrase())
                    .build());
            errorMessage.setStatus(status);
            errorMessage.setMessage(String.format("Erro ao executar SQL na base de dados. (SQLGrammarException) %s", errors));
            msgCause = errorMessage.getMessage();
        }
        log.error("Erro ao executar um procecimento. Classe -> {} | Mensagem -> {} | Causa -> {}",
                classStackTrace,
                e.getMessage(),
                msgCause);

        return errorMessage;

    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StandarError> trataErros(ResponseStatusException e) {

        return ResponseEntity
                .status(e.getStatusCode())
                .body(new StandarError(null, e.getReason(), "", System.currentTimeMillis(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrosDTO trataErros(MethodArgumentNotValidException e) {
        List<String> erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + " - " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiErrosDTO(erros);
    }

}
