package org.efa.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class StandartResponseBusiness implements IStandardResponseBusiness {
    @Value("${dev.info.enabled:false}")
    private boolean devInfoEnabled;

    @Override
    public StandartResponse build(HttpStatus httpStatus, Throwable ex, String message) {
        StandartResponse sr=new StandartResponse();
        sr.setDevInfoEnabled(devInfoEnabled);
        sr.setMessage(message);
        sr.setHttpStatus(httpStatus);
        sr.setEx(ex);
        return sr;
    }

}
