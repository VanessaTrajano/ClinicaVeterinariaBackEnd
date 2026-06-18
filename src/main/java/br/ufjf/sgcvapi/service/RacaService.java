package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.model.entity.Raca;
import br.ufjf.sgcvapi.model.repository.RacaRepository;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RacaService {
    private RacaRepository repository;

    public RacaService(RacaRepository repository){
        this.repository = repository;
    }

    public List<Raca> getRacas() {
        return repository.findAll();
    }

    public Optional<Raca> getRacaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Raca salvar(Raca raca) {
        validar(raca);
        return repository.save(raca);
    }

    @Transactional
    public void excluir(Raca raca) {
        Objects.requireNonNull(raca.getId());
        repository.delete(raca);
    }

    public void validar(Raca raca) {
        if (raca.getNome() == null || raca.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (raca.getEspecie() == null || raca.getEspecie().getId() == null || raca.getEspecie().getId() == 0) {
            throw new RegraNegocioException("Especie inválida");
        }
    }
}
