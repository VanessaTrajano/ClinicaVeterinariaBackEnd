package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.MedicamentoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Medicamento;
import br.ufjf.sgcvapi.service.MedicamentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/medicamentos")
@RequiredArgsConstructor
@CrossOrigin
public class MedicamentoController {

    private final MedicamentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Medicamento> medicamentos = service.getMedicamentos();
        return ResponseEntity.ok(medicamentos.stream().map(MedicamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Medicamento> medicamento = service.getMedicamentoById(id);
        if (!medicamento.isPresent()) {
            return new ResponseEntity("Medicamento não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(medicamento.map(MedicamentoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody MedicamentoDTO dto) {
        try {
            Medicamento medicamento = converter(dto);
            medicamento = service.salvar(medicamento);
            return new ResponseEntity(medicamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MedicamentoDTO dto) {
        if (!service.getMedicamentoById(id).isPresent()) {
            return new ResponseEntity("Medicamento não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Medicamento medicamento = converter(dto);
            medicamento.setId(id);
            service.salvar(medicamento);
            return ResponseEntity.ok(medicamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Medicamento> medicamento = service.getMedicamentoById(id);
        if (!medicamento.isPresent()) {
            return new ResponseEntity("Medicamento não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(medicamento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Medicamento converter(MedicamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Medicamento medicamento = modelMapper.map(dto, Medicamento.class);
        return medicamento;
    }
}
