package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
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

    public MedicamentoConsultaService(MedicamentoConsultaRepository repository) {
        this.repository = repository;
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
        return repository.save(medicamentoConsulta);
    }

    @Transactional
    public void excluir(MedicamentoConsulta medicamentoConsulta) {
        Objects.requireNonNull(medicamentoConsulta.getId());
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
}
