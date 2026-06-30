package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.RacaDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especie;
import br.ufjf.sgcvapi.model.entity.Raca;
import br.ufjf.sgcvapi.service.EspecieService;
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
@RequestMapping("/api/v1/racas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Raças", description = "Operações relacionadas a raças")
public class RacaController {

    private final RacaService service;
    private final EspecieService especieService;

    @GetMapping()
    @Operation(summary = "Lista todas as raças")
    public ResponseEntity get() {
        List<Raca> racas = service.getRacas();
        return ResponseEntity.ok(racas.stream().map(RacaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma raça pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Raca> raca = service.getRacaById(id);
        if (!raca.isPresent()) {
            return new ResponseEntity("Raca não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(raca.map(RacaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova raça")
    public ResponseEntity post(@RequestBody RacaDTO dto) {
        try {
            Raca raca = converter(dto);
            raca = service.salvar(raca);
            return new ResponseEntity(raca, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de uma raça existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody RacaDTO dto) {
        if (!service.getRacaById(id).isPresent()) {
            return new ResponseEntity("Raca não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Raca raca = converter(dto);
            raca.setId(id);
            service.salvar(raca);
            return ResponseEntity.ok(raca);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma raça")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Raca> raca = service.getRacaById(id);
        if (!raca.isPresent()) {
            return new ResponseEntity("Raca não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(raca.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Raca converter(RacaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Raca raca = modelMapper.map(dto, Raca.class);
        Optional<Especie> especie = especieService.getEspecieById(dto.getIdEspecie());
        if(!especie.isPresent()) {
            throw new RegraNegocioException("Espécie não encontrada");
        }
        raca.setEspecie(especie.get());
        return raca;
    }
}
