package com.digitalinnovationone.summershirts.service;

import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.entity.Shirt;
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
        Optional<Shirt> optSavedShirtModel = shirtRepository.findByModel(shirtDTO.getModel());
        if (optSavedShirtModel.isPresent()) {
            throw new ShirtWithThisModelAlreadyRegisteredException(shirtDTO.getModel().getDescription());
        }
        Shirt shirt = new Shirt(
                shirtDTO.getId(),
                shirtDTO.getBrand(),
                shirtDTO.getMax(),
                shirtDTO.getQuantity(),
                shirtDTO.getModel());

        shirtRepository.save(shirt);

        ShirtDTO shirtDTO1 = new ShirtDTO(
                shirt.getId(), shirtDTO.getBrand(),
                shirtDTO.getMax(),
                shirtDTO.getQuantity(),
                shirtDTO.getModel());
        return shirtDTO1;
    }
}