package br.com.bytestorm.mark_point.exception;

import br.com.bytestorm.mark_point.enums.EnumException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final EnumException enumException;

    public ServiceException(String message) {
        super(message);
        this.enumException = EnumException.DEFAULT;
        log.info(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.enumException = EnumException.DEFAULT;
        log.error(message);
    }

    public ServiceException(String message, EnumException e) {
        super(message);
        this.enumException = e;
        log.info(message);
    }

    public ServiceException(String msg, Throwable cause, EnumException e) {
        super(msg, cause);
        this.enumException = e;
        log.info(msg);
    }
}
