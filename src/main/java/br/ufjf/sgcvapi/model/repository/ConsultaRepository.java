package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
