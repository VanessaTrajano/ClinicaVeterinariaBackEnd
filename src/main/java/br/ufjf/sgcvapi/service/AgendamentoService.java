package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Agendamento;
import br.ufjf.sgcvapi.model.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgendamentoService {
    private AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public List<Agendamento> getAgendamentos() {
        return repository.findAll();
    }

    public Optional<Agendamento> getAgendamentoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Agendamento salvar(Agendamento agendamento) {
        validar(agendamento);
        return repository.save(agendamento);
    }

    @Transactional
    public void excluir(Agendamento agendamento) {
        Objects.requireNonNull(agendamento.getId());
        repository.delete(agendamento);
    }

    public void validar(Agendamento agendamento) {
        if (agendamento.getServico() == null || agendamento.getServico().getId() == null || agendamento.getServico().getId() == 0) {
            throw new RegraNegocioException("Servico inválido");
        }
        if (agendamento.getDisponibilidade() == null || agendamento.getDisponibilidade().getId() == null || agendamento.getDisponibilidade().getId() == 0) {
            throw new RegraNegocioException("Disponibilidade inválida");
        }
        if (agendamento.getPet() == null || agendamento.getPet().getId() == null || agendamento.getPet().getId() == 0) {
            throw new RegraNegocioException("Pet inválido");
        }
        if (agendamento.getCliente() == null || agendamento.getCliente().getId() == null || agendamento.getCliente().getId() == 0) {
            throw new RegraNegocioException("Cliente inválido");
        }
        if (agendamento.getConsulta() == null || agendamento.getConsulta().getId() == null || agendamento.getConsulta().getId() == 0) {
            throw new RegraNegocioException("Consulta inválida");
        }
    }
}
