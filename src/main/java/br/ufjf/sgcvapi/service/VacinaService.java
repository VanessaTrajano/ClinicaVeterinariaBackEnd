package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Vacina;
import br.ufjf.sgcvapi.model.repository.VacinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VacinaService {
    private VacinaRepository repository;

    public VacinaService(VacinaRepository repository) {
        this.repository = repository;
    }

    public List<Vacina> getVacinas() {
        return repository.findAll();
    }

    public Optional<Vacina> getVacinaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Vacina salvar(Vacina vacina) {
        validar(vacina);
        return repository.save(vacina);
    }

    @Transactional
    public void excluir(Vacina vacina) {
        Objects.requireNonNull(vacina.getId());
        repository.delete(vacina);
    }

    public void validar(Vacina vacina) {
        if (vacina.getValor() == null || vacina.getValor() == 0) {
            throw new RegraNegocioException("Valor inválido");
        }
        if (vacina.getNome() == null || vacina.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (vacina.getNumLote() == null || vacina.getNumLote().trim().equals("")) {
            throw new RegraNegocioException("Número de Lote inválido");
        }
        if (vacina.getDescricao() == null || vacina.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição inválida");
        }
    }
}
