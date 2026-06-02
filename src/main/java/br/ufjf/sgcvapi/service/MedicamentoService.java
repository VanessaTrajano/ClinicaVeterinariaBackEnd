package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Medicamento;
import br.ufjf.sgcvapi.model.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MedicamentoService {
    private MedicamentoRepository repository;

    public MedicamentoService(MedicamentoRepository repository) {
        this.repository = repository;
    }

    public List<Medicamento> getMedicamentos() {
        return repository.findAll();
    }

    public Optional<Medicamento> getMedicamentoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Medicamento salvar(Medicamento medicamento) {
        validar(medicamento);
        return repository.save(medicamento);
    }

    @Transactional
    public void excluir(Medicamento medicamento) {
        Objects.requireNonNull(medicamento.getId());
        repository.delete(medicamento);
    }

    public void validar(Medicamento medicamento) {
        if (medicamento.getValor() == null || medicamento.getValor() == 0) {
            throw new RegraNegocioException("Valor inválido");
        }
        if (medicamento.getNome() == null || medicamento.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (medicamento.getIum() == null || medicamento.getIum().trim().equals("")) {
            throw new RegraNegocioException("IUM inválido");
        }
        if (medicamento.getDescricao() == null || medicamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição inválido");
        }
    }
}
