package org.efa.backend.model.business.exceptions;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends Exception {

    private static final long serialVersionUID = 1652404412196966252L;

    @Builder
    public NotFoundException(String message, Throwable ex) {
        super(message, ex);
    }

    @Builder
    public NotFoundException(String message) {
        super(message);
    }

    @Builder
    public NotFoundException(Throwable ex) {
        super(ex.getMessage(), ex);
    }
}
