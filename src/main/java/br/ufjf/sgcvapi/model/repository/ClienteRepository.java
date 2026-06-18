package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
}
