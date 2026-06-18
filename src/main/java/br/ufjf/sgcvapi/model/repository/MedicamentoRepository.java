package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long>{
}
