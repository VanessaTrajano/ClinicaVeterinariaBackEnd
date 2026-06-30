package br.ufjf.sgcvapi.service;

import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Pet;
import br.ufjf.sgcvapi.model.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PetService {
    private PetRepository repository;

    public PetService(PetRepository repository) {
        this.repository = repository;
    }

    public List<Pet> getPets() {
        return repository.findAll();
    }

    public Optional<Pet> getPetById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Pet salvar(Pet pet) {
        validar(pet);
        return repository.save(pet);
    }

    @Transactional
    public void excluir(Pet pet) {
        Objects.requireNonNull(pet.getId());
        repository.delete(pet);
    }

    public void validar(Pet pet) {
        if (pet.getPeso() <= 0) {
            throw new RegraNegocioException("Peso inválido");
        }
        if (pet.getNome() == null || pet.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (pet.getDataDeNascimento() == null || pet.getDataDeNascimento().trim().equals("")) {
            throw new RegraNegocioException("Data de Nascimento inválida");
        }
        if (pet.getSexo() == null || pet.getSexo().trim().equals("")) {
            throw new RegraNegocioException("Sexo inválido");
        }
        if (pet.getRaca() == null || pet.getRaca().getId() == null || pet.getRaca().getId() == 0) {
            throw new RegraNegocioException("Raça inválido");
        }
        if (pet.getDono() == null || pet.getDono().getId() == null || pet.getDono().getId() == 0) {
            throw new RegraNegocioException("Dono inválido");
        }
    }
}
