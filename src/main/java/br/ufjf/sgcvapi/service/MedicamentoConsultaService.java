package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.MedicamentoConsulta;
import br.ufjf.sgcvapi.model.repository.MedicamentoConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MedicamentoConsultaService {
    private MedicamentoConsultaRepository repository;

    private final ConsultaService consultaService;

    public MedicamentoConsultaService(MedicamentoConsultaRepository repository,
                                      ConsultaService consultaService) {
        this.repository = repository;
        this.consultaService = consultaService;
    }

    public List<MedicamentoConsulta> getMedicamentoConsultas() {
        return repository.findAll();
    }

    public Optional<MedicamentoConsulta> getMedicamentoConsultaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MedicamentoConsulta salvar(MedicamentoConsulta medicamentoConsulta) {
        validar(medicamentoConsulta);
        MedicamentoConsulta medicamentoConsultaSalvo = repository.save(medicamentoConsulta);
        atualizarValorTotalConsulta(medicamentoConsultaSalvo);
        return medicamentoConsultaSalvo;
    }

    @Transactional
    public void excluir(MedicamentoConsulta medicamentoConsulta) {
        Objects.requireNonNull(medicamentoConsulta.getId());
        subtrairValorTotalConsulta(medicamentoConsulta);
        repository.delete(medicamentoConsulta);
    }

    public void validar(MedicamentoConsulta medicamentoConsulta) {
        if (medicamentoConsulta.getMedicamento() == null || medicamentoConsulta.getMedicamento().getId() == null || medicamentoConsulta.getMedicamento().getId() == 0) {
            throw new RegraNegocioException("Medicamento inválido");
        }
        if (medicamentoConsulta.getConsulta() == null || medicamentoConsulta.getConsulta().getId() == null || medicamentoConsulta.getConsulta().getId() == 0) {
            throw new RegraNegocioException("Consulta inválida");
        }
    }

    private void atualizarValorTotalConsulta(MedicamentoConsulta medicamentoConsulta) {
        Long idConsulta = medicamentoConsulta.getConsulta().getId();

        Consulta consulta = consultaService.getConsultaById(idConsulta)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada para atualização de valor."));

        float valorMedicamento = medicamentoConsulta.getMedicamento().getValor();

        float novoValorTotal = consulta.getValor() + valorMedicamento;
        consulta.setValor(novoValorTotal);

        consultaService.salvar(consulta);
    }

    private void subtrairValorTotalConsulta(MedicamentoConsulta medicamentoConsulta) {
        Long idConsulta = medicamentoConsulta.getConsulta().getId();

        Consulta consulta = consultaService.getConsultaById(idConsulta)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada para subtração de valor."));

        float valorMedicamento = medicamentoConsulta.getMedicamento().getValor();

        float novoValorTotal = consulta.getValor() - valorMedicamento;
        if (novoValorTotal < 0) {
            novoValorTotal = 0;
        }
        consulta.setValor(novoValorTotal);
        
        consultaService.salvar(consulta);
    }
}
