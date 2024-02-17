package org.efa.backend.model.business.exceptions;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FoundException extends Exception {


    private static final long serialVersionUID = 1947762836757854370L;

    @Builder
    public FoundException(String message, Throwable ex) {
        super(message, ex);
    }
    @Builder
    public FoundException(String message) {
        super(message);
    }
    @Builder
    public FoundException(Throwable ex) {
        super(ex.getMessage(), ex);
    }
}
