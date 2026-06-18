package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long>{
}
