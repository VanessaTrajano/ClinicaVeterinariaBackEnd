package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.ConsultaServicoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.ConsultaServico;
import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Servico;
import br.ufjf.sgcvapi.service.ConsultaService;
import br.ufjf.sgcvapi.service.ConsultaServicoService;
import br.ufjf.sgcvapi.service.ServicoService;
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
@RequestMapping("/api/v1/consultaServicos")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "ConsultaServicos", description = "Operações relacionadas à ligação de serviços e consultas já cadastrados")
public class ConsultaServicoController {

    private final ConsultaServicoService service;
    private final ConsultaService consultaService;
    private final ServicoService servicoService;

    @GetMapping()
    @Operation(summary = "Lista todas as consultaServicos")
    public ResponseEntity get() {
        List<ConsultaServico> consultaServicos = service.getConsultaServicos();
        return ResponseEntity.ok(consultaServicos.stream().map(ConsultaServicoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma ConsultaServicos por ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ConsultaServico> consultaServico = service.getConsultaServicoById(id);
        if (!consultaServico.isPresent()) {
            return new ResponseEntity("ConsultaServico não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(consultaServico.map(ConsultaServicoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova ConsultaServico")
    public ResponseEntity post(@RequestBody ConsultaServicoDTO dto) {
        try {
            ConsultaServico consultaServico = converter(dto);
            consultaServico = service.salvar(consultaServico);
            return new ResponseEntity(consultaServico, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de uma ConsultaServico existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ConsultaServicoDTO dto) {
        if (!service.getConsultaServicoById(id).isPresent()) {
            return new ResponseEntity("ConsultaServico não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ConsultaServico consultaServico = converter(dto);
            consultaServico.setId(id);
            service.salvar(consultaServico);
            return ResponseEntity.ok(consultaServico);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma ConsultaServico")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ConsultaServico> consultaServico = service.getConsultaServicoById(id);
        if (!consultaServico.isPresent()) {
            return new ResponseEntity("ConsultaServico não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(consultaServico.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ConsultaServico converter(ConsultaServicoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ConsultaServico consultaServico = modelMapper.map(dto, ConsultaServico.class);
        Optional<Consulta> consulta = consultaService.getConsultaById(dto.getIdConsulta());
        if(!consulta.isPresent()) {
            throw new RegraNegocioException("Consulta não encontrada");
        }
        consultaServico.setConsulta(consulta.get());
        Optional<Servico> servico = servicoService.getServicoById(dto.getIdServico());
        if(!servico.isPresent()) {
            throw new RegraNegocioException("Serviço não encontrada");
        }
        consultaServico.setServico(servico.get());
        return consultaServico;
    }
}