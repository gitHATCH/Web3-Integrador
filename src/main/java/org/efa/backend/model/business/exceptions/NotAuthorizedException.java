package org.efa.backend.model.business.exceptions;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotAuthorizedException extends Exception{
  @Builder
  public NotAuthorizedException(String message, Throwable ex) {
      super(message, ex);
  }

  @Builder
  public NotAuthorizedException(String message) {
      super(message);
  }

  @Builder
  public NotAuthorizedException(Throwable ex) {
      super(ex.getMessage(), ex);
  }



}
