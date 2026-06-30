package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.MedicamentoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoConsultaRepository extends JpaRepository<MedicamentoConsulta, Long>{
    boolean existsByConsultaId(Long consultaId);
}
