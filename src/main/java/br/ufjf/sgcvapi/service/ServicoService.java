package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Servico;
import br.ufjf.sgcvapi.model.repository.ServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServicoService {
    private ServicoRepository repository;

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public List<Servico> getServicos() {
        return repository.findAll();
    }

    public Optional<Servico> getServicoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Servico salvar(Servico servico) {
        validar(servico);
        return repository.save(servico);
    }

    @Transactional
    public void excluir(Servico servico) {
        Objects.requireNonNull(servico.getId());
        repository.delete(servico);
    }

    public void validar(Servico servico) {
        if (servico.getValor() == 0) {
            throw new RegraNegocioException("Valor inválido");
        }
        if (servico.getNome() == null || servico.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (servico.getDescricao() == null || servico.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descrição inválido");
        }
    }
}
