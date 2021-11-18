package com.digitalinnovationone.summershirts.controller;

import com.digitalinnovationone.summershirts.dto.QuantityDTO;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtStockDecrementExceededException;
import com.digitalinnovationone.summershirts.exception.ShirtStockIncrementExceededException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages shirt stock")
public interface ShirtControllerDocs {

    @ApiOperation(value = "Shirt creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success shirt creation"),
            @ApiResponse(code = 400, message = "missing required fields or wrong filed rang value.")
    })
    ShirtDTO createShirt(ShirtDTO shirtDTO) throws ShirtWithThisModelAlreadyRegisteredException;

    @ApiOperation(value = "Returns shirt found by a given model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to found shirt in system"),
            @ApiResponse(code = 404, message = "Shirt with this model not found in system.")
    })
    ShirtDTO findByModel(ShirtModel shirtModel) throws ShirtNotFoundException;

    @ApiOperation(value = "Returns a list of all shirts registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all shirts registered in system"),
    })
    List<ShirtDTO> listAll();

    @ApiOperation(value = "Delete a shirt found by a given valid id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success to delete shirt from system"),
            @ApiResponse(code = 404, message = "Shirt with given id not found")
    })
    void deleteById(@PathVariable Long id) throws ShirtNotFoundException;

    @ApiOperation(value = "Execute increment operation and returns a actualized shirt")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to actualize the shirt incrementation"),
            @ApiResponse(code = 404, message = "Shirt stock increment operation not found."),
            @ApiResponse(code = 400, message = "Max capacity exceeded trying to increment stock")
    })
    ShirtDTO increment(@PathVariable Long id, QuantityDTO quantityDTO) throws ShirtStockIncrementExceededException, ShirtNotFoundException;

    @ApiOperation(value = "Execute decrement operation and returns a actualized shirt")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to actualize the shirt decrementation"),
            @ApiResponse(code = 404, message = "Shirt stock decrement operation not found."),
            @ApiResponse(code = 400, message = "Min capacity exceeded trying to decrement stock")
    })
    ShirtDTO decrement(@PathVariable Long id, QuantityDTO quantityDTO) throws ShirtStockDecrementExceededException, ShirtNotFoundException;
}
