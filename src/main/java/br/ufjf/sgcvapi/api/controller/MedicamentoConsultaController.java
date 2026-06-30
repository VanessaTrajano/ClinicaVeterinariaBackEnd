package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.MedicamentoConsultaDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.MedicamentoConsulta;
import br.ufjf.sgcvapi.model.entity.Medicamento;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.service.ConsultaService;
import br.ufjf.sgcvapi.service.MedicamentoConsultaService;
import br.ufjf.sgcvapi.service.MedicamentoService;
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
@RequestMapping("/api/v1/medicamentoConsultas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "MedicamentoConsultas", description = "Operações relacionadas à ligação de medicamentos e consultas já cadastrados")
public class MedicamentoConsultaController {

    private final MedicamentoConsultaService service;
    private final MedicamentoService medicamentoService;
    private final ConsultaService consultaService;

    @GetMapping()
    @Operation(summary = "Lista todos os MedicamentoConsultas")
    public ResponseEntity get() {
        List<MedicamentoConsulta> medicamentoConsultas = service.getMedicamentoConsultas();
        return ResponseEntity.ok(medicamentoConsultas.stream().map(MedicamentoConsultaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um MedicamentoConsulta por ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<MedicamentoConsulta> medicamentoConsulta = service.getMedicamentoConsultaById(id);
        if (!medicamentoConsulta.isPresent()) {
            return new ResponseEntity("MedicamentoConsulta não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(medicamentoConsulta.map(MedicamentoConsultaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo MedicamentoConsulta")
    public ResponseEntity post(@RequestBody MedicamentoConsultaDTO dto) {
        try {
            MedicamentoConsulta medicamentoConsulta = converter(dto);
            medicamentoConsulta = service.salvar(medicamentoConsulta);
            return new ResponseEntity(medicamentoConsulta, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza dados de um MedicamentoConsulta existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MedicamentoConsultaDTO dto) {
        if (!service.getMedicamentoConsultaById(id).isPresent()) {
            return new ResponseEntity("MedicamentoConsulta não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            MedicamentoConsulta medicamentoConsulta = converter(dto);
            medicamentoConsulta.setId(id);
            service.salvar(medicamentoConsulta);
            return ResponseEntity.ok(medicamentoConsulta);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um MedicamentoConsulta")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<MedicamentoConsulta> medicamentoConsulta = service.getMedicamentoConsultaById(id);
        if (!medicamentoConsulta.isPresent()) {
            return new ResponseEntity("MedicamentoConsulta não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(medicamentoConsulta.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public MedicamentoConsulta converter(MedicamentoConsultaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        MedicamentoConsulta medicamentoConsulta = modelMapper.map(dto, MedicamentoConsulta.class);
        Optional<Medicamento> medicamento = medicamentoService.getMedicamentoById(dto.getIdMedicamento());
        if(!medicamento.isPresent()) {
            throw new RegraNegocioException("Medicamento não encontrado");
        }
        medicamentoConsulta.setMedicamento(medicamento.get());
        Optional<Consulta> consulta = consultaService.getConsultaById(dto.getIdConsulta());
        if(!consulta.isPresent()) {
            throw new RegraNegocioException("Consulta não encontrada");
        }
        medicamentoConsulta.setConsulta(consulta.get());
        return medicamentoConsulta;
    }
}
