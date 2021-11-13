package com.digitalinnovationone.summershirts.dto;

import com.digitalinnovationone.summershirts.enums.ShirtModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShirtDTO {

    private Long id;

    @NotNull
    private String brand;

    @NotNull
    @Max(100)
    private int max;

    @NotNull
    @Max(2)
    private int quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ShirtModel model;
}
