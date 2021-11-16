package com.digitalinnovationone.summershirts.exception;

import com.digitalinnovationone.summershirts.enums.ShirtModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShirtNotFoundException extends Exception {
    public ShirtNotFoundException(ShirtModel model) {
        super(String.format("Shirt with model %s not found in the system.", model));
    }
}
