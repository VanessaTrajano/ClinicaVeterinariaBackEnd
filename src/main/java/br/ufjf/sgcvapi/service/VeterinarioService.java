package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import br.ufjf.sgcvapi.model.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VeterinarioService {
    private VeterinarioRepository repository;

    public VeterinarioService(VeterinarioRepository repository) {
        this.repository = repository;
    }

    public List<Veterinario> getVeterinarios() {
        return repository.findAll();
    }

    public Optional<Veterinario> getVeterinarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Veterinario salvar(Veterinario veterinario) {
        validar(veterinario);
        return repository.save(veterinario);
    }

    @Transactional
    public void excluir(Veterinario veterinario) {
        Objects.requireNonNull(veterinario.getId());
        repository.delete(veterinario);
    }

    public void validar(Veterinario veterinario) {
        if (veterinario.getNome() == null || veterinario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (veterinario.getCpf() == null || veterinario.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (veterinario.getEmail() == null || veterinario.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
        if (veterinario.getCelular() == null || veterinario.getCelular().trim().equals("")) {
            throw new RegraNegocioException("Celular inválido");
        }
        if (veterinario.getEndereco() == null || veterinario.getEndereco().getId() == null || veterinario.getEndereco().getId() == 0) {
            throw new RegraNegocioException("Endereço inválido");
        }
        if (veterinario.getCrmv() == null || veterinario.getCrmv().trim().equals("")) {
            throw new RegraNegocioException("CRMV inválido");
        }
    }
}
