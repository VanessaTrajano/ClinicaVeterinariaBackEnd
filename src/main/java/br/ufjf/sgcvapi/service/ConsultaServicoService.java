package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.ConsultaServico;
import br.ufjf.sgcvapi.model.repository.ConsultaServicoRepository;
import br.ufjf.sgcvapi.model.repository.MedicamentoConsultaRepository;
import br.ufjf.sgcvapi.model.repository.VacinaConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaServicoService {
    private ConsultaServicoRepository repository;

    private final VacinaConsultaRepository vacinaConsultaRepository;
    private final MedicamentoConsultaRepository medicamentoConsultaRepository;

    public ConsultaServicoService(ConsultaServicoRepository repository,
                                  VacinaConsultaRepository vacinaConsultaRepository,
                                  MedicamentoConsultaRepository medicamentoConsultaRepository) {
        this.repository = repository;
        this.vacinaConsultaRepository = vacinaConsultaRepository;
        this.medicamentoConsultaRepository = medicamentoConsultaRepository;
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

        Long idConsulta = consultaServico.getConsulta().getId();

        if (Boolean.TRUE.equals(consultaServico.getServico().isVacinaEhObrigatorio())) {
            boolean possuiVacina = vacinaConsultaRepository.existsByConsultaId(idConsulta);
            if (!possuiVacina) {
                throw new RegraNegocioException("Não é possível adicionar este serviço. Ele exige uma vacina obrigatória, mas nenhuma vacina foi vinculada a esta consulta ainda.");
            }
        }
        if (Boolean.TRUE.equals(consultaServico.getServico().isMedicamentoEhObrigatorio())) {
            boolean possuiMedicamento = medicamentoConsultaRepository.existsByConsultaId(idConsulta);
            if (!possuiMedicamento) {
                throw new RegraNegocioException("Não é possível adicionar este serviço. Ele exige um medicamento obrigatório, mas nenhum medicamento foi vinculado a esta consulta ainda.");
            }
        }
    }
}
