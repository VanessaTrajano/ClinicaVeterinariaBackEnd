package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacinaRepository extends JpaRepository<Vacina, Long>{
}
