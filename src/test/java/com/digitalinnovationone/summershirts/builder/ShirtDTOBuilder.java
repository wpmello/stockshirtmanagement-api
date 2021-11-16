package com.digitalinnovationone.summershirts.builder;

import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import lombok.Builder;

/*
    this class makes it easier the creation of an object to execute the tests
 */

@Builder
public class ShirtDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String brand = "Adidas";

    @Builder.Default
    private int max = 100;

    @Builder.Default
    private int quantity = 100;

    @Builder.Default
    private ShirtModel model = ShirtModel.TOUR;

    public ShirtDTO toShirtDTO() {
        return new ShirtDTO(id, brand, max, quantity, model);
    }
}
