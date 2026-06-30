package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Consulta;
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

    private final ConsultaService consultaService;

    public ConsultaServicoService(ConsultaServicoRepository repository,
                                  VacinaConsultaRepository vacinaConsultaRepository,
                                  MedicamentoConsultaRepository medicamentoConsultaRepository,
                                  ConsultaService consultaService) {
        this.repository = repository;
        this.vacinaConsultaRepository = vacinaConsultaRepository;
        this.medicamentoConsultaRepository = medicamentoConsultaRepository;
        this.consultaService = consultaService;
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
        ConsultaServico consultaServicoSalvo = repository.save(consultaServico);
        atualizarValorTotalConsulta(consultaServicoSalvo);
        return consultaServicoSalvo;
    }

    @Transactional
    public void excluir(ConsultaServico consultaServico) {
        Objects.requireNonNull(consultaServico.getId());
        subtrairValorTotalConsulta(consultaServico);
        repository.delete(consultaServico);
    }

    public void validar(ConsultaServico consultaServico) {
        if (consultaServico.getQuantServicos() == null || consultaServico.getQuantServicos() == 0) {
            throw new RegraNegocioException("Quantidade de Serviços inválidos");
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

    private void atualizarValorTotalConsulta(ConsultaServico consultaServico) {
        Long idConsulta = consultaServico.getConsulta().getId();

        Consulta consulta = consultaService.getConsultaById(idConsulta).orElseThrow(() -> new RegraNegocioException("Consulta não encontrada para atualização de valor."));

        float valorServico = consultaServico.getServico().getValor();
        int quantidade = consultaServico.getQuantServicos();
        float subtotal = valorServico * quantidade;

        float novoValorTotal = consulta.getValor() + subtotal;
        consulta.setValor(novoValorTotal);

        consultaService.salvar(consulta);
    }

    private void subtrairValorTotalConsulta(ConsultaServico consultaServico) {
        Long idConsulta = consultaServico.getConsulta().getId();

        Consulta consulta = consultaService.getConsultaById(idConsulta)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada para subtração de valor."));

        float valorServico = consultaServico.getServico().getValor();
        int quantidade = consultaServico.getQuantServicos();
        float subtotal = valorServico * quantidade;

        float novoValorTotal = consulta.getValor() - subtotal;
        if (novoValorTotal < 0) {
            novoValorTotal = 0;
        }
        consulta.setValor(novoValorTotal);

        consultaService.salvar(consulta);
    }
}
