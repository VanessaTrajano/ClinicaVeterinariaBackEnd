package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
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

    public VacinaConsultaService(VacinaConsultaRepository repository) {
        this.repository = repository;
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
        return repository.save(vacinaConsulta);
    }

    @Transactional
    public void excluir(VacinaConsulta vacinaConsulta) {
        Objects.requireNonNull(vacinaConsulta.getId());
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
}
