package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Disponibilidade;
import br.ufjf.sgcvapi.model.repository.DisponibilidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DisponibilidadeService {
    private DisponibilidadeRepository repository;

    public DisponibilidadeService(DisponibilidadeRepository repository) {
        this.repository = repository;
    }

    public List<Disponibilidade> getDisponibilidades() {
        return repository.findAll();
    }

    public Optional<Disponibilidade> getDisponibilidadeById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        validar(disponibilidade);
        return repository.save(disponibilidade);
    }

    @Transactional
    public void excluir(Disponibilidade disponibilidade) {
        Objects.requireNonNull(disponibilidade.getId());
        repository.delete(disponibilidade);
    }

    public void validar(Disponibilidade disponibilidade) {
        if (disponibilidade.getData() == null || disponibilidade.getData().trim().equals("")) {
            throw new RegraNegocioException("Data inválida");
        }
        if (disponibilidade.getHorario() == null || disponibilidade.getHorario().trim().equals("")) {
            throw new RegraNegocioException("Horário inválido");
        }
        if (disponibilidade.getVeterinario() == null || disponibilidade.getVeterinario().getId() == null || disponibilidade.getVeterinario().getId() == 0) {
            throw new RegraNegocioException("Veterinário inválido");
        }
    }
}
