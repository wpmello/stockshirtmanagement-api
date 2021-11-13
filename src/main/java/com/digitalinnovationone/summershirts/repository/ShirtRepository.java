package com.digitalinnovationone.summershirts.repository;

import com.digitalinnovationone.summershirts.entity.Shirt;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShirtRepository extends JpaRepository<Shirt, Long> {

    Optional<Shirt> findByModel(ShirtModel model);
}
