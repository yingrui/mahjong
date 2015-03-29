package me.yingrui.segment.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
final public class NotFoundException extends RuntimeException {
    public NotFoundException() {

    }

    public NotFoundException(String message) {
        super(message);
    }
}
