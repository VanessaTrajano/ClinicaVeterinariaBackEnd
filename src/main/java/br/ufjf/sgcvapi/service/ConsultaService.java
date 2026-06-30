package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Pet;
import br.ufjf.sgcvapi.model.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConsultaService {
    private ConsultaRepository repository;

    private final PetService petService;

    public ConsultaService(ConsultaRepository repository, PetService petService) {
        this.repository = repository;
        this.petService = petService;
    }

    public List<Consulta> getConsultas() {
        return repository.findAll();
    }

    public Optional<Consulta> getConsultaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Consulta salvar(Consulta consulta) {

        if (consulta.getId() == null) {
            consulta.setValor(0);
        }

        validar(consulta);

        Consulta consultaSalva = repository.save(consulta);
        if (consultaSalva.getQuantDiasInternacao() != null && consultaSalva.getQuantDiasInternacao() > 0) {
            Pet pet = consultaSalva.getPet();
            if (pet != null) {
                pet.setEstaInternado(true);
                petService.salvar(pet);
            }
        }

        return consultaSalva;
    }

    @Transactional
    public void excluir(Consulta consulta) {
        Objects.requireNonNull(consulta.getId());
        repository.delete(consulta);
    }

    public void validar(Consulta consulta) {
        if (consulta.getQuantDiasInternacao() == null || consulta.getQuantDiasInternacao() < 0) {
            throw new RegraNegocioException("Quantidade de dias de internação inválida");
        }
        if (consulta.getId() == null && consulta.getValor() != 0) {
            throw new RegraNegocioException("Uma nova consulta deve ser criada inicialmente com o valor zerado.");
        }
        if (consulta.getValor() < 0) {
            throw new RegraNegocioException("O valor da consulta não pode ser negativo.");
        }
        if (consulta.getStatus() == null || consulta.getStatus().trim().equals("")) {
            throw new RegraNegocioException("Status inválido");
        }
        if (consulta.getQuantDiasInternacao() > 0 && (consulta.getMotivoInternacao() == null || consulta.getMotivoInternacao().trim().equals(""))) {
            throw new RegraNegocioException("Motivo da Internação inválido");
        }
        if (consulta.getDiagnostico() == null || consulta.getDiagnostico().trim().equals("")) {
            throw new RegraNegocioException("Diagnóstico inválido");
        }
        if (consulta.getReceita() == null || consulta.getReceita().trim().equals("")) {
            throw new RegraNegocioException("Receita inválida");
        }
        if (consulta.getPet() == null || consulta.getPet().getId() == null || consulta.getPet().getId() == 0) {
            throw new RegraNegocioException("Pet inválido");
        }
    }
}
