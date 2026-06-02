package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.VeterinarioEspecializacao;
import br.ufjf.sgcvapi.model.repository.VeterinarioEspecializacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VeterinarioEspecializacaoService {
    private VeterinarioEspecializacaoRepository repository;

    public VeterinarioEspecializacaoService(VeterinarioEspecializacaoRepository repository) {
        this.repository = repository;
    }

    public List<VeterinarioEspecializacao> getVeterinarioEspecializacaos() {
        return repository.findAll();
    }

    public Optional<VeterinarioEspecializacao> getVeterinarioEspecializacaoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public VeterinarioEspecializacao salvar(VeterinarioEspecializacao veterinarioEspecializacao) {
        validar(veterinarioEspecializacao);
        return repository.save(veterinarioEspecializacao);
    }

    @Transactional
    public void excluir(VeterinarioEspecializacao veterinarioEspecializacao) {
        Objects.requireNonNull(veterinarioEspecializacao.getId());
        repository.delete(veterinarioEspecializacao);
    }

    public void validar(VeterinarioEspecializacao veterinarioEspecializacao) {
        if (veterinarioEspecializacao.getVeterinario() == null || veterinarioEspecializacao.getVeterinario().getId() == null || veterinarioEspecializacao.getVeterinario().getId() == 0) {
            throw new RegraNegocioException("Veterinario inválido");
        }
        if (veterinarioEspecializacao.getEspecializacao() == null || veterinarioEspecializacao.getEspecializacao().getId() == null || veterinarioEspecializacao.getEspecializacao().getId() == 0) {
            throw new RegraNegocioException("Especialização inválido");
        }
    }
}
