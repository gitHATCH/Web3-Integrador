package org.efa.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StandartResponse {
    private String message;

    @JsonIgnore
    private Throwable ex;

    @JsonIgnore
    private HttpStatus httpStatus;

    @JsonIgnore
    private boolean devInfoEnabled;

    public String getMessage() {
        if(message!=null)
            return message;
        if (ex!=null)
            return ex.getMessage();
        return null;
    }
}
