package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long>{
}
