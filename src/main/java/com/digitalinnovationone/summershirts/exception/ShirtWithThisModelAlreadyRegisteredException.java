package com.digitalinnovationone.summershirts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShirtWithThisModelAlreadyRegisteredException extends Exception {
    public ShirtWithThisModelAlreadyRegisteredException(String model) {
        super(String.format("Shirt with model '%s' already registered in the system.", model));
    }
}
