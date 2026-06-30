package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.ConsultaVeterinarioDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.ConsultaVeterinario;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import br.ufjf.sgcvapi.service.ConsultaService;
import br.ufjf.sgcvapi.service.ConsultaVeterinarioService;
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
@RequestMapping("/api/v1/consultaVeterinarios")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "ConsultaVeterinarios", description = "Operações relacionadas à ligação de veterinários e consultas já cadastrados")
public class ConsultaVeterinarioController {

    private final ConsultaVeterinarioService service;
    private final ConsultaService consultaService;
    private final VeterinarioService veterinarioService;

    @GetMapping()
    @Operation(summary = "Lista todos as ConsultaVeterinarios")
    public ResponseEntity get() {
        List<ConsultaVeterinario> consultaVeterinarios = service.getConsultaVeterinarios();
        return ResponseEntity.ok(consultaVeterinarios.stream().map(ConsultaVeterinarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma ConsultaVeterinario pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ConsultaVeterinario> consultaVeterinario = service.getConsultaVeterinarioById(id);
        if (!consultaVeterinario.isPresent()) {
            return new ResponseEntity("ConsultaVeterinario não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(consultaVeterinario.map(ConsultaVeterinarioDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova ConsultaVeterinario")
    public ResponseEntity post(@RequestBody ConsultaVeterinarioDTO dto) {
        try {
            ConsultaVeterinario consultaVeterinario = converter(dto);
            consultaVeterinario = service.salvar(consultaVeterinario);
            return new ResponseEntity(consultaVeterinario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de uma ConsultaVeterinario já existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ConsultaVeterinarioDTO dto) {
        if (!service.getConsultaVeterinarioById(id).isPresent()) {
            return new ResponseEntity("ConsultaVeterinario não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ConsultaVeterinario consultaVeterinario = converter(dto);
            consultaVeterinario.setId(id);
            service.salvar(consultaVeterinario);
            return ResponseEntity.ok(consultaVeterinario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma ConsultaVeterinario")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ConsultaVeterinario> consultaVeterinario = service.getConsultaVeterinarioById(id);
        if (!consultaVeterinario.isPresent()) {
            return new ResponseEntity("ConsultaVeterinario não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(consultaVeterinario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ConsultaVeterinario converter(ConsultaVeterinarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ConsultaVeterinario consultaVeterinario = modelMapper.map(dto, ConsultaVeterinario.class);
        Optional<Consulta> consulta = consultaService.getConsultaById(dto.getIdConsulta());
        if(!consulta.isPresent()) {
            throw new RegraNegocioException("Consulta não encontrada");
        }
        consultaVeterinario.setConsulta(consulta.get());
        Optional<Veterinario> veterinario = veterinarioService.getVeterinarioById(dto.getIdVeterinario());
        if(!veterinario.isPresent()) {
            throw new RegraNegocioException("Veterinário não encontrada");
        }
        consultaVeterinario.setVeterinario(veterinario.get());
        return consultaVeterinario;
    }
}