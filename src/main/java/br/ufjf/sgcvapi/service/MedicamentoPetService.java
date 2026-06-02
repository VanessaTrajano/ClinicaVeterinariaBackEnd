package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.MedicamentoPet;
import br.ufjf.sgcvapi.model.repository.MedicamentoPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MedicamentoPetService {
    private MedicamentoPetRepository repository;

    public MedicamentoPetService(MedicamentoPetRepository repository) {
        this.repository = repository;
    }

    public List<MedicamentoPet> getMedicamentoPets() {
        return repository.findAll();
    }

    public Optional<MedicamentoPet> getMedicamentoPetById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MedicamentoPet salvar(MedicamentoPet medicamentoPet) {
        validar(medicamentoPet);
        return repository.save(medicamentoPet);
    }

    @Transactional
    public void excluir(MedicamentoPet medicamentoPet) {
        Objects.requireNonNull(medicamentoPet.getId());
        repository.delete(medicamentoPet);
    }

    public void validar(MedicamentoPet medicamentoPet) {
        if (medicamentoPet.getAlergia() == null || medicamentoPet.getAlergia().getId() == null || medicamentoPet.getAlergia().getId() == 0) {
            throw new RegraNegocioException("Alergia inválida");
        }
        if (medicamentoPet.getPet() == null || medicamentoPet.getPet().getId() == null || medicamentoPet.getPet().getId() == 0) {
            throw new RegraNegocioException("Pet inválido");
        }
    }
}
