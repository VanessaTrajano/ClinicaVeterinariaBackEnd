package br.ufjf.sgcvapi.api.controller;


import br.ufjf.sgcvapi.api.dto.EspecieDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especie;
import br.ufjf.sgcvapi.service.EspecieService;
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
@RequestMapping("/api/v1/especies")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Espécies", description = "Operações relacionadas a espécies")
public class EspecieController {
    private final EspecieService service;

    @GetMapping()
    @Operation(summary = "Lista todas as espécies")
    public ResponseEntity get() {
        List<Especie> especies = service.getEspecies();
        return ResponseEntity.ok(especies.stream().map(EspecieDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma espécie pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Especie não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especie.map(EspecieDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova espécie")
    public ResponseEntity post(@RequestBody EspecieDTO dto) {
        try {
            Especie especie = converter(dto);
            especie = service.salvar(especie);
            return new ResponseEntity(especie, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de uma espécie existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EspecieDTO dto) {
        if (!service.getEspecieById(id).isPresent()) {
            return new ResponseEntity("Especie não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Especie especie = converter(dto);
            especie.setId(id);
            service.salvar(especie);
            return ResponseEntity.ok(especie);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma espécie")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Especie> especie = service.getEspecieById(id);
        if (!especie.isPresent()) {
            return new ResponseEntity("Especie não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(especie.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Especie converter(EspecieDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Especie especie = modelMapper.map(dto, Especie.class);
        return especie;
    }
}
