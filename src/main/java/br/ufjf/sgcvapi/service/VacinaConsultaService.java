package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.VacinaConsulta;
import br.ufjf.sgcvapi.model.repository.VacinaConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VacinaConsultaService {
    private VacinaConsultaRepository repository;

    private final ConsultaService consultaService;

    public VacinaConsultaService(VacinaConsultaRepository repository,
                                 ConsultaService consultaService) {
        this.repository = repository;
        this.consultaService = consultaService;
    }

    public List<VacinaConsulta> getVacinaConsultas() {
        return repository.findAll();
    }

    public Optional<VacinaConsulta> getVacinaConsultaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public VacinaConsulta salvar(VacinaConsulta vacinaConsulta) {
        validar(vacinaConsulta);
        VacinaConsulta vacinaConsultaSalva = repository.save(vacinaConsulta);
        atualizarValorTotalConsulta(vacinaConsultaSalva);
        return vacinaConsultaSalva;
    }

    @Transactional
    public void excluir(VacinaConsulta vacinaConsulta) {
        Objects.requireNonNull(vacinaConsulta.getId());
        subtrairValorTotalConsulta(vacinaConsulta);
        repository.delete(vacinaConsulta);
    }

    public void validar(VacinaConsulta vacinaConsulta) {
        if (vacinaConsulta.getVacina() == null || vacinaConsulta.getVacina().getId() == null || vacinaConsulta.getVacina().getId() == 0) {
            throw new RegraNegocioException("Vacina inválida");
        }
        if (vacinaConsulta.getConsulta() == null || vacinaConsulta.getConsulta().getId() == null || vacinaConsulta.getConsulta().getId() == 0) {
            throw new RegraNegocioException("Consulta inválida");
        }
    }

    private void atualizarValorTotalConsulta(VacinaConsulta vacinaConsulta) {
        Long idConsulta = vacinaConsulta.getConsulta().getId();

        Consulta consulta = consultaService.getConsultaById(idConsulta)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada para atualização de valor."));

        float valorVacina = vacinaConsulta.getVacina().getValor();

        float novoValorTotal = consulta.getValor() + valorVacina;
        consulta.setValor(novoValorTotal);

        consultaService.salvar(consulta);
    }

    private void subtrairValorTotalConsulta(VacinaConsulta vacinaConsulta) {
        Long idConsulta = vacinaConsulta.getConsulta().getId();

        Consulta consulta = consultaService.getConsultaById(idConsulta)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada para subtração de valor."));

        float valorVacina = vacinaConsulta.getVacina().getValor();

        float novoValorTotal = consulta.getValor() - valorVacina;
        if (novoValorTotal < 0) {
            novoValorTotal = 0;
        }
        consulta.setValor(novoValorTotal);

        consultaService.salvar(consulta);
    }
}
