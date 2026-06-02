package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.ConsultaVeterinario;
import br.ufjf.sgcvapi.model.repository.ConsultaVeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaVeterinarioService {
    private ConsultaVeterinarioRepository repository;

    public ConsultaVeterinarioService(ConsultaVeterinarioRepository repository) {
        this.repository = repository;
    }

    public List<ConsultaVeterinario> getConsultaVeterinarios() {
        return repository.findAll();
    }

    public Optional<ConsultaVeterinario> getConsultaVeterinarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ConsultaVeterinario salvar(ConsultaVeterinario consultaVeterinario) {
        validar(consultaVeterinario);
        return repository.save(consultaVeterinario);
    }

    @Transactional
    public void excluir(ConsultaVeterinario consultaVeterinario) {
        Objects.requireNonNull(consultaVeterinario.getId());
        repository.delete(consultaVeterinario);
    }

    public void validar(ConsultaVeterinario consultaVeterinario) {
        if (consultaVeterinario.getConsulta() == null || consultaVeterinario.getConsulta().getId() == null || consultaVeterinario.getConsulta().getId() == 0) {
            throw new RegraNegocioException("Consulta inválida");
        }
        if (consultaVeterinario.getVeterinario() == null || consultaVeterinario.getVeterinario().getId() == null || consultaVeterinario.getVeterinario().getId() == 0) {
            throw new RegraNegocioException("Veterinário inválido");
        }
    }
}
