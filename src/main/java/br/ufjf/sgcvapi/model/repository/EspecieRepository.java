package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieRepository extends JpaRepository<Especie, Long>{
}
