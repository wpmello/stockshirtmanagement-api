package com.digitalinnovationone.summershirts.service;

import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.entity.Shirt;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.repository.ShirtRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShirtService {

    private ShirtRepository shirtRepository;

    public ShirtDTO createShirt(ShirtDTO shirtDTO) throws ShirtWithThisModelAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(shirtDTO);
        Shirt shirt = toModel(shirtDTO);
        shirtRepository.save(shirt);
        ShirtDTO shirtDTO1 = toDTO(shirt);

        return shirtDTO1;
    }

    public ShirtDTO findByModel(ShirtModel model) throws ShirtNotFoundException {
        Shirt foundShirt = shirtRepository.findByModel(model)
                .orElseThrow(() -> new ShirtNotFoundException(model));
        return toDTO(foundShirt);

    }

    // it's simplification of the method -instance a dto from an entity-
    private ShirtDTO toDTO(Shirt shirt) {
        ShirtDTO shirtDTO = new ShirtDTO(
                shirt.getId(),
                shirt.getBrand(),
                shirt.getMax(),
                shirt.getQuantity(),
                shirt.getModel());
        return shirtDTO;
    }

    // it's simplification of the method -instance an entity from a dto-
    private Shirt toModel(ShirtDTO shirtDTO) {
        Shirt shirt = new Shirt(
                shirtDTO.getId(),
                shirtDTO.getBrand(),
                shirtDTO.getMax(),
                shirtDTO.getQuantity(),
                shirtDTO.getModel());
        return shirt;
    }

    // It's simplification of the method 'findByModel()'
    private void verifyIfIsAlreadyRegistered(ShirtDTO shirtDTO) throws ShirtWithThisModelAlreadyRegisteredException {
        Optional<Shirt> optSavedShirtModel = shirtRepository.findByModel(shirtDTO.getModel());
        if (optSavedShirtModel.isPresent()) {
            throw new ShirtWithThisModelAlreadyRegisteredException(shirtDTO.getModel().getDescription());
        }
    }
}