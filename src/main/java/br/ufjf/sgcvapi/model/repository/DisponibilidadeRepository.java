package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Disponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long>{
}
