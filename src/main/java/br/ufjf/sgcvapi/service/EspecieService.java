package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especie;
import br.ufjf.sgcvapi.model.repository.EspecieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EspecieService {
    private EspecieRepository repository;

    public EspecieService(EspecieRepository repository){
        this.repository = repository;
    }

    public List<Especie> getEspecies() {
        return repository.findAll();
    }

    public Optional<Especie> getEspecieById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Especie salvar(Especie especie) {
        validar(especie);
        return repository.save(especie);
    }

    @Transactional
    public void excluir(Especie especie) {
        Objects.requireNonNull(especie.getId());
        repository.delete(especie);
    }

    public void validar(Especie especie) {
        if (especie.getNome() == null || especie.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
    }
}
