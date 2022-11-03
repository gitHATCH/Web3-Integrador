package org.efa.backend.utils;

import org.springframework.http.HttpStatus;

public interface IStandardResponseBusiness {
    StandardResponse build(HttpStatus httpStatus, Throwable ex, String message);
}
