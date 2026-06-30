package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.PetDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Cliente;
import br.ufjf.sgcvapi.model.entity.Raca;
import br.ufjf.sgcvapi.model.entity.Pet;
import br.ufjf.sgcvapi.service.ClienteService;
import br.ufjf.sgcvapi.service.PetService;
import br.ufjf.sgcvapi.service.RacaService;
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
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Pets", description = "Operações relacionadas a pets")
public class PetController {

    private final PetService service;
    private final RacaService racaService;
    private final ClienteService clienteService;

    @GetMapping()
    @Operation(summary = "Lista todos os pets")
    public ResponseEntity get() {
        List<Pet> pets = service.getPets();
        return ResponseEntity.ok(pets.stream().map(PetDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um pet pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Pet> pet = service.getPetById(id);
        if (!pet.isPresent()) {
            return new ResponseEntity("Pet não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pet.map(PetDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo pet")
    public ResponseEntity post(@RequestBody PetDTO dto) {
        try {
            Pet pet = converter(dto);
            pet = service.salvar(pet);
            return new ResponseEntity(pet, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um pet existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PetDTO dto) {
        if (!service.getPetById(id).isPresent()) {
            return new ResponseEntity("Pet não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Pet pet = converter(dto);
            pet.setId(id);
            service.salvar(pet);
            return ResponseEntity.ok(pet);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um pet")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Pet> pet = service.getPetById(id);
        if (!pet.isPresent()) {
            return new ResponseEntity("Pet não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(pet.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Pet converter(PetDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Pet pet = modelMapper.map(dto, Pet.class);
        Optional<Raca> raca = racaService.getRacaById(dto.getIdRaca());
        if(!raca.isPresent()) {
            throw new RegraNegocioException("Raça não encontrada");
        }
        pet.setRaca(raca.get());
        Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdDono());
        if(!cliente.isPresent()) {
            throw new RegraNegocioException("Dono não encontrado");
        }
        pet.setDono(cliente.get());
        return pet;
    }
}