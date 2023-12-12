package org.efa.backend.exceptions.custom;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusinessException extends Exception{

    private static final long serialVersionUID = -8582277206660722157L;
    @Builder
    public BusinessException(String message, Throwable ex) {
        super(message, ex);
    }
    @Builder
    public BusinessException(String message) {
        super(message);
    }
    @Builder
    public BusinessException(Throwable ex) {
        super(ex.getMessage(), ex);
    }
}
