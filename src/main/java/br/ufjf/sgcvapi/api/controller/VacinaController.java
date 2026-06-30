package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.VacinaDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Vacina;
import br.ufjf.sgcvapi.service.VacinaService;
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
@RequestMapping("/api/v1/vacinas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Vacinas", description = "Operações relacionadas a vacinas")
public class VacinaController {

    private final VacinaService service;

    @GetMapping()
    @Operation(summary = "Lista todas as vacinas")
    public ResponseEntity get() {
        List<Vacina> vacinas = service.getVacinas();
        return ResponseEntity.ok(vacinas.stream().map(VacinaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma vacina pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Vacina> vacina = service.getVacinaById(id);
        if (!vacina.isPresent()) {
            return new ResponseEntity("Vacina não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vacina.map(VacinaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova vacina")
    public ResponseEntity post(@RequestBody VacinaDTO dto) {
        try {
            Vacina vacina = converter(dto);
            vacina = service.salvar(vacina);
            return new ResponseEntity(vacina, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza dados de uma vacina existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VacinaDTO dto) {
        if (!service.getVacinaById(id).isPresent()) {
            return new ResponseEntity("Vacina não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Vacina vacina = converter(dto);
            vacina.setId(id);
            service.salvar(vacina);
            return ResponseEntity.ok(vacina);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma vacina")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Vacina> vacina = service.getVacinaById(id);
        if (!vacina.isPresent()) {
            return new ResponseEntity("Vacina não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(vacina.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Vacina converter(VacinaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Vacina vacina = modelMapper.map(dto, Vacina.class);
        return vacina;
    }
}