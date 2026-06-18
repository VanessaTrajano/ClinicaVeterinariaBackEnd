package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.ServicoEspecializacao;
import br.ufjf.sgcvapi.model.repository.ServicoEspecializacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServicoEspecializacaoService {
    private ServicoEspecializacaoRepository repository;

    public ServicoEspecializacaoService(ServicoEspecializacaoRepository repository) {
        this.repository = repository;
    }

    public List<ServicoEspecializacao> getServicoEspecializacaos() {
        return repository.findAll();
    }

    public Optional<ServicoEspecializacao> getServicoEspecializacaoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ServicoEspecializacao salvar(ServicoEspecializacao servicoEspecializacao) {
        validar(servicoEspecializacao);
        return repository.save(servicoEspecializacao);
    }

    @Transactional
    public void excluir(ServicoEspecializacao servicoEspecializacao) {
        Objects.requireNonNull(servicoEspecializacao.getId());
        repository.delete(servicoEspecializacao);
    }

    public void validar(ServicoEspecializacao servicoEspecializacao) {
        if (servicoEspecializacao.getServico() == null || servicoEspecializacao.getServico().getId() == null || servicoEspecializacao.getServico().getId() == 0) {
            throw new RegraNegocioException("Serviço inválido");
        }
        if (servicoEspecializacao.getEspecializacao() == null || servicoEspecializacao.getEspecializacao().getId() == null || servicoEspecializacao.getEspecializacao().getId() == 0) {
            throw new RegraNegocioException("Especialização inválida");
        }
    }
}
