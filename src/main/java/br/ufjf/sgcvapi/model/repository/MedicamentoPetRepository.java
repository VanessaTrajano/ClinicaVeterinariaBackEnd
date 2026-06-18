package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.MedicamentoPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoPetRepository extends JpaRepository<MedicamentoPet, Long>{
}
