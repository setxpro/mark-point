package br.com.bytestorm.mark_point.exception;

import br.com.bytestorm.mark_point.enums.EnumException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final EnumException enumException;

    public NegocioException(String message) {
        super(message);
        this.enumException = EnumException.DEFAULT;
        log.info(message);
    }

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
        this.enumException = EnumException.DEFAULT;
        log.error(message);
    }

    public NegocioException(String message, EnumException e) {
        super(message);
        this.enumException = e;
        log.info(message);
    }

    public NegocioException(String msg, Throwable cause, EnumException e) {
        super(msg, cause);
        this.enumException = e;
        log.info(msg);
    }
}
