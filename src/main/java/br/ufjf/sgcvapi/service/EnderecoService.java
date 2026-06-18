package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Endereco;
import br.ufjf.sgcvapi.model.repository.EnderecoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EnderecoService {
    private EnderecoRepository repository;

    public EnderecoService(EnderecoRepository repository) {
        this.repository = repository;
    }

    public List<Endereco> getEnderecos() {
        return repository.findAll();
    }

    public Optional<Endereco> getEnderecoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Endereco salvar(Endereco endereco) {
        validar(endereco);
        return repository.save(endereco);
    }

    @Transactional
    public void excluir(Endereco endereco) {
        Objects.requireNonNull(endereco.getId());
        repository.delete(endereco);
    }

    public void validar(Endereco endereco) {
        if (endereco.getNumero() == null || endereco.getNumero() == 0) {
            throw new RegraNegocioException("Numero inválido");
        }
        if (endereco.getComplemento() == null || endereco.getComplemento().trim().equals("")) {
            throw new RegraNegocioException("Complemento inválido");
        }
        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }
        if (endereco.getBairro() == null || endereco.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro inválido");
        }
        if (endereco.getCidade() == null || endereco.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade inválido");
        }
        if (endereco.getEstado() == null || endereco.getEstado().trim().equals("")) {
            throw new RegraNegocioException("Estado inválido");
        }
    }
}
