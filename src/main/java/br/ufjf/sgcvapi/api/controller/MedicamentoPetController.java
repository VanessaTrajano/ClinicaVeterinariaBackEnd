package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.MedicamentoPetDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Pet;
import br.ufjf.sgcvapi.model.entity.Medicamento;
import br.ufjf.sgcvapi.model.entity.MedicamentoPet;
import br.ufjf.sgcvapi.service.MedicamentoPetService;
import br.ufjf.sgcvapi.service.MedicamentoService;
import br.ufjf.sgcvapi.service.PetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/medicamentoPets")
@RequiredArgsConstructor
@CrossOrigin
public class MedicamentoPetController {

    private final MedicamentoPetService service;
    private final MedicamentoService medicamentoService;
    private final PetService petService;

    @GetMapping()
    public ResponseEntity get() {
        List<MedicamentoPet> medicamentoPets = service.getMedicamentoPets();
        return ResponseEntity.ok(medicamentoPets.stream().map(MedicamentoPetDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<MedicamentoPet> medicamentoPet = service.getMedicamentoPetById(id);
        if (!medicamentoPet.isPresent()) {
            return new ResponseEntity("MedicamentoPet não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(medicamentoPet.map(MedicamentoPetDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody MedicamentoPetDTO dto) {
        try {
            MedicamentoPet medicamentoPet = converter(dto);
            medicamentoPet = service.salvar(medicamentoPet);
            return new ResponseEntity(medicamentoPet, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MedicamentoPetDTO dto) {
        if (!service.getMedicamentoPetById(id).isPresent()) {
            return new ResponseEntity("MedicamentoPet não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            MedicamentoPet medicamentoPet = converter(dto);
            medicamentoPet.setId(id);
            service.salvar(medicamentoPet);
            return ResponseEntity.ok(medicamentoPet);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<MedicamentoPet> medicamentoPet = service.getMedicamentoPetById(id);
        if (!medicamentoPet.isPresent()) {
            return new ResponseEntity("MedicamentoPet não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(medicamentoPet.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public MedicamentoPet converter(MedicamentoPetDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        MedicamentoPet medicamentoPet = modelMapper.map(dto, MedicamentoPet.class);
        Optional<Medicamento> medicamento = medicamentoService.getMedicamentoById(dto.getIdAlergia());
        if(!medicamento.isPresent()) {
            throw new RegraNegocioException("Medicamento não encontrada");
        }
        medicamentoPet.setAlergia(medicamento.get());
        Optional<Pet> pet = petService.getPetById(dto.getIdPet());
        if(!pet.isPresent()) {
            throw new RegraNegocioException("Pet não encontrada");
        }
        medicamentoPet.setPet(pet.get());
        return medicamentoPet;
    }
}
