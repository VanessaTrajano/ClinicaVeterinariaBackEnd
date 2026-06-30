package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.VacinaConsultaDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Vacina;
import br.ufjf.sgcvapi.model.entity.VacinaConsulta;
import br.ufjf.sgcvapi.service.ConsultaService;
import br.ufjf.sgcvapi.service.VacinaConsultaService;
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
@RequestMapping("/api/v1/vacinaConsultas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "VacinaConsultas", description = "Operações relacionadas à ligação de vacinas e consultas existentes")
public class VacinaConsultaController {

    private final VacinaConsultaService service;
    private final VacinaService vacinaService;
    private final ConsultaService consultaService;

    @GetMapping()
    @Operation(summary = "Lista todas as vacinaConsultas")
    public ResponseEntity get() {
        List<VacinaConsulta> vacinaConsultas = service.getVacinaConsultas();
        return ResponseEntity.ok(vacinaConsultas.stream().map(VacinaConsultaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma vacinaConsulta pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<VacinaConsulta> vacinaConsulta = service.getVacinaConsultaById(id);
        if (!vacinaConsulta.isPresent()) {
            return new ResponseEntity("VacinaConsulta não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vacinaConsulta.map(VacinaConsultaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova vacinaConsulta")
    public ResponseEntity post(@RequestBody VacinaConsultaDTO dto) {
        try {
            VacinaConsulta vacinaConsulta = converter(dto);
            vacinaConsulta = service.salvar(vacinaConsulta);
            return new ResponseEntity(vacinaConsulta, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza dados de uma vacinaConsulta existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VacinaConsultaDTO dto) {
        if (!service.getVacinaConsultaById(id).isPresent()) {
            return new ResponseEntity("VacinaConsulta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            VacinaConsulta vacinaConsulta = converter(dto);
            vacinaConsulta.setId(id);
            service.salvar(vacinaConsulta);
            return ResponseEntity.ok(vacinaConsulta);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma vacinaConsulta")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<VacinaConsulta> vacinaConsulta = service.getVacinaConsultaById(id);
        if (!vacinaConsulta.isPresent()) {
            return new ResponseEntity("VacinaConsulta não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(vacinaConsulta.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public VacinaConsulta converter(VacinaConsultaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        VacinaConsulta vacinaConsulta = modelMapper.map(dto, VacinaConsulta.class);
        Optional<Vacina> vacina = vacinaService.getVacinaById(dto.getIdVacina());
        if(!vacina.isPresent()) {
            throw new RegraNegocioException("Vacina não encontrada");
        }
        vacinaConsulta.setVacina(vacina.get());
        Optional<Consulta> consulta = consultaService.getConsultaById(dto.getIdConsulta());
        if(!consulta.isPresent()) {
            throw new RegraNegocioException("Consulta não encontrada");
        }
        vacinaConsulta.setConsulta(consulta.get());
        return vacinaConsulta;
    }
}