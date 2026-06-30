package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.MedicamentoPetDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Pet;
import br.ufjf.sgcvapi.model.entity.Medicamento;
import br.ufjf.sgcvapi.model.entity.MedicamentoPet;
import br.ufjf.sgcvapi.service.MedicamentoPetService;
import br.ufjf.sgcvapi.service.MedicamentoService;
import br.ufjf.sgcvapi.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "MedicamentoPets", description = "Operações relacionadas à ligação de medicamentos e pets já existentes, se referenciando à alergias dos pets")
public class MedicamentoPetController {

    private final MedicamentoPetService service;
    private final MedicamentoService medicamentoService;
    private final PetService petService;

    @GetMapping()
    @Operation(summary = "Lista todos os MedicamentoPets")
    public ResponseEntity get() {
        List<MedicamentoPet> medicamentoPets = service.getMedicamentoPets();
        return ResponseEntity.ok(medicamentoPets.stream().map(MedicamentoPetDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um MedicamentoPet pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<MedicamentoPet> medicamentoPet = service.getMedicamentoPetById(id);
        if (!medicamentoPet.isPresent()) {
            return new ResponseEntity("MedicamentoPet não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(medicamentoPet.map(MedicamentoPetDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo MedicamentoPet")
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
    @Operation(summary = "Atualiza os dados de um MedicamentoPet existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MedicamentoPetDTO dto) {
        if (!service.getMedicamentoPetById(id).isPresent()) {
            return new ResponseEntity("MedicamentoPet não encontrado", HttpStatus.NOT_FOUND);
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
    @Operation(summary = "Exclui um MedicamentoPet")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<MedicamentoPet> medicamentoPet = service.getMedicamentoPetById(id);
        if (!medicamentoPet.isPresent()) {
            return new ResponseEntity("MedicamentoPet não encontrado", HttpStatus.NOT_FOUND);
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
            throw new RegraNegocioException("Medicamento não encontrado");
        }
        medicamentoPet.setAlergia(medicamento.get());
        Optional<Pet> pet = petService.getPetById(dto.getIdPet());
        if(!pet.isPresent()) {
            throw new RegraNegocioException("Pet não encontrado");
        }
        medicamentoPet.setPet(pet.get());
        return medicamentoPet;
    }
}
