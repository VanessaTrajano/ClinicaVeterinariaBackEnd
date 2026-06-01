package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especializacao;
import br.ufjf.sgcvapi.model.repository.EspecializacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EspecializacaoService {
    private EspecializacaoRepository repository;

    public EspecializacaoService(EspecializacaoRepository repository){
        this.repository = repository;
    }

    public List<Especializacao> getEspecializacoes() {
        return repository.findAll();
    }

    public Optional<Especializacao> getEspecializacaoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Especializacao salvar(Especializacao especializacao) {
        validar(especializacao);
        return repository.save(especializacao);
    }

    @Transactional
    public void excluir(Especializacao especializacao) {
        Objects.requireNonNull(especializacao.getId());
        repository.delete(especializacao);
    }

    public void validar(Especializacao especializacao) {
        if (especializacao.getNome() == null || especializacao.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
    }
}
