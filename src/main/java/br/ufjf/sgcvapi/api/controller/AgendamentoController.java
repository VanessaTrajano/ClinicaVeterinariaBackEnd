package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.AgendamentoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.*;
import br.ufjf.sgcvapi.service.*;
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
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Agendamentos", description = "Operações relacionadas a agendamentos de consultas")
public class AgendamentoController {

    private final AgendamentoService service;
    private final ServicoService servicoService;
    private final PetService petService;
    private final ClienteService clienteService;
    private final DisponibilidadeService disponibilidadeService;
    private final ConsultaService consultaService;

    @GetMapping()
    @Operation(summary = "Lista todos os agendamentos")
    public ResponseEntity get() {
        List<Agendamento> agendamentos = service.getAgendamentos();
        return ResponseEntity.ok(agendamentos.stream().map(AgendamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um agendamento pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Agendamento> agendamento = service.getAgendamentoById(id);
        if (!agendamento.isPresent()) {
            return new ResponseEntity("Agendamento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(agendamento.map(AgendamentoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo agendamento")
    public ResponseEntity post(@RequestBody AgendamentoDTO dto) {
        try {
            Agendamento agendamento = converter(dto);
            agendamento = service.salvar(agendamento);
            return new ResponseEntity(agendamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um agendamento existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AgendamentoDTO dto) {
        if (!service.getAgendamentoById(id).isPresent()) {
            return new ResponseEntity("Agendamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Agendamento agendamento = converter(dto);
            agendamento.setId(id);
            service.salvar(agendamento);
            return ResponseEntity.ok(agendamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um agendamento")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Agendamento> agendamento = service.getAgendamentoById(id);
        if (!agendamento.isPresent()) {
            return new ResponseEntity("Agendamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(agendamento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Agendamento converter(AgendamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Agendamento agendamento = modelMapper.map(dto, Agendamento.class);
        Optional<Servico> servico = servicoService.getServicoById(dto.getIdServico());
        if(!servico.isPresent()) {
            throw new RegraNegocioException("Servico não encontrado");
        }
        agendamento.setServico(servico.get());
        Optional<Disponibilidade> disponibilidade = disponibilidadeService.getDisponibilidadeById(dto.getIdDisponibilidade());
        if(!disponibilidade.isPresent()) {
            throw new RegraNegocioException("Disponibilidade não encontrada");
        }
        agendamento.setDisponibilidade(disponibilidade.get());
        Optional<Pet> pet = petService.getPetById(dto.getIdPet());
        if(!pet.isPresent()) {
            throw new RegraNegocioException("Pet não encontrado");
        }
        agendamento.setPet(pet.get());
        Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
        if(!cliente.isPresent()) {
            throw new RegraNegocioException("Cliente não encontrado");
        }
        agendamento.setCliente(cliente.get());
        Optional<Consulta> consulta = consultaService.getConsultaById(dto.getIdConsulta());
        if(!consulta.isPresent()) {
            throw new RegraNegocioException("Consulta não encontrada");
        }
        agendamento.setConsulta(consulta.get());
        return agendamento;
    }
}
