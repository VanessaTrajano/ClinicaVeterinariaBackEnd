package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.ConsultaDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Pet;
import br.ufjf.sgcvapi.service.ConsultaService;
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
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
@CrossOrigin
public class ConsultaController {

    private final ConsultaService service;
    private final PetService petService;

    @GetMapping()
    public ResponseEntity get() {
        List<Consulta> consultas = service.getConsultas();
        return ResponseEntity.ok(consultas.stream().map(ConsultaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(consulta.map(ConsultaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ConsultaDTO dto) {
        try {
            Consulta consulta = converter(dto);
            consulta = service.salvar(consulta);
            return new ResponseEntity(consulta, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ConsultaDTO dto) {
        if (!service.getConsultaById(id).isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Consulta consulta = converter(dto);
            consulta.setId(id);
            service.salvar(consulta);
            return ResponseEntity.ok(consulta);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Consulta> consulta = service.getConsultaById(id);
        if (!consulta.isPresent()) {
            return new ResponseEntity("Consulta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(consulta.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Consulta converter(ConsultaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Consulta consulta = modelMapper.map(dto, Consulta.class);
        Optional<Pet> pet = petService.getPetById(dto.getIdPet());
        if(!pet.isPresent()) {
            throw new RegraNegocioException("Pet não encontrada");
        }
        cliente.setPet(pet.get());
        return consulta;
    }
}
