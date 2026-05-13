package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
}
