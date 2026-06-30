package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.DisponibilidadeDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Disponibilidade;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import br.ufjf.sgcvapi.service.DisponibilidadeService;
import br.ufjf.sgcvapi.service.VeterinarioService;
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
@RequestMapping("/api/v1/disponibilidades")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Disponibilidades", description = "Operações relacionadas a disponibilidades de veterinários")
public class DisponibilidadeController {

    private final DisponibilidadeService service;
    private final VeterinarioService veterinarioService;

    @GetMapping()
    @Operation(summary = "Lista todos as disponibilidades")
    public ResponseEntity get() {
        List<Disponibilidade> disponibilidades = service.getDisponibilidades();
        return ResponseEntity.ok(disponibilidades.stream().map(DisponibilidadeDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma disponibilidade pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Disponibilidade> disponibilidade = service.getDisponibilidadeById(id);
        if (!disponibilidade.isPresent()) {
            return new ResponseEntity("Disponibilidade não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(disponibilidade.map(DisponibilidadeDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova disponibilidade")
    public ResponseEntity post(@RequestBody DisponibilidadeDTO dto) {
        try {
            Disponibilidade disponibilidade = converter(dto);
            disponibilidade = service.salvar(disponibilidade);
            return new ResponseEntity(disponibilidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de uma disponibilidade existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody DisponibilidadeDTO dto) {
        if (!service.getDisponibilidadeById(id).isPresent()) {
            return new ResponseEntity("Disponibilidade não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Disponibilidade disponibilidade = converter(dto);
            disponibilidade.setId(id);
            service.salvar(disponibilidade);
            return ResponseEntity.ok(disponibilidade);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma disponibilidade")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Disponibilidade> disponibilidade = service.getDisponibilidadeById(id);
        if (!disponibilidade.isPresent()) {
            return new ResponseEntity("Disponibilidade não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(disponibilidade.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Disponibilidade converter(DisponibilidadeDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Disponibilidade disponibilidade = modelMapper.map(dto, Disponibilidade.class);
        Optional<Veterinario> veterinario = veterinarioService.getVeterinarioById(dto.getIdVeterinario());
        if(!veterinario.isPresent()) {
            throw new RegraNegocioException("Veterinário não encontrada");
        }
        disponibilidade.setVeterinario(veterinario.get());
        return disponibilidade;
    }
}
