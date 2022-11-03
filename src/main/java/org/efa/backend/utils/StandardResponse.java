package org.efa.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.exception.ExceptionUtils;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StandardResponse {
    private String message;

    @JsonIgnore
    private Throwable ex;

    @JsonIgnore
    private HttpStatus httpStatus;

    public int getCode() {
        return httpStatus.value();
    }

    @JsonIgnore
    private boolean devInfoEnabled;

    public String getDevInfo() {
        if(devInfoEnabled) {
            if(ex!=null) {
                return ExceptionUtils.getStackTrace(ex);
            } else {
                return "No stack trace";
            }
        } else {
            return null;
        }
    }

    public String getMessage() {
        if(message!=null)
            return message;
        if (ex!=null)
            return ex.getMessage();
        return null;
    }
}
