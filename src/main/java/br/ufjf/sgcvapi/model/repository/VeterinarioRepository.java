package br.ufjf.sgcvapi.model.repository;

import br.ufjf.sgcvapi.model.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long>{
}
