package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.ConsultaServico;
import br.ufjf.sgcvapi.model.repository.ConsultaServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaServicoService {
    private ConsultaServicoRepository repository;

    public ConsultaServicoService(ConsultaServicoRepository repository) {
        this.repository = repository;
    }

    public List<ConsultaServico> getConsultaServicos() {
        return repository.findAll();
    }

    public Optional<ConsultaServico> getConsultaServicoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ConsultaServico salvar(ConsultaServico consultaServico) {
        validar(consultaServico);
        return repository.save(consultaServico);
    }

    @Transactional
    public void excluir(ConsultaServico consultaServico) {
        Objects.requireNonNull(consultaServico.getId());
        repository.delete(consultaServico);
    }

    public void validar(ConsultaServico consultaServico) {
        if (consultaServico.getQuantServicos() == null || consultaServico.getQuantServicos() == 0) {
            throw new RegraNegocioException("Quantidade de Serviços inválida");
        }
        if (consultaServico.getConsulta() == null || consultaServico.getConsulta().getId() == null || consultaServico.getConsulta().getId() == 0) {
            throw new RegraNegocioException("Consulta inválida");
        }
        if (consultaServico.getServico() == null || consultaServico.getServico().getId() == null || consultaServico.getServico().getId() == 0) {
            throw new RegraNegocioException("Servico inválido");
        }
    }
}
