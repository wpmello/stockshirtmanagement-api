package com.digitalinnovationone.summershirts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShirtStockDecrementExceededException extends Exception{
    public ShirtStockDecrementExceededException(Long id, int quantityToDecrement) {
        super(String.format("Shirt with ID %d informed to increment exceeds the max stock capacity. " +
                "Informed quantity: %d. Verify max quantity from product and try again!", id, quantityToDecrement));
    }
}
