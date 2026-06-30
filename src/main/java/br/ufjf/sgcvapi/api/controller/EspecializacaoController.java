package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.EspecializacaoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especializacao;
import br.ufjf.sgcvapi.service.EspecializacaoService;
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
@RequestMapping("/api/v1/especializacoes")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Especializações", description = "Operações relacionadas a especializações")
public class EspecializacaoController {

    private final EspecializacaoService service;

    @GetMapping()
    @Operation(summary = "Lista todas as especializações")
    public ResponseEntity get() {
        List<Especializacao> especializacoes = service.getEspecializacoes();
        return ResponseEntity.ok(especializacoes.stream().map(EspecializacaoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta uma especialização pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Especializacao> especializacao = service.getEspecializacaoById(id);
        if (!especializacao.isPresent()) {
            return new ResponseEntity("Especialização não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(especializacao.map(EspecializacaoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra uma nova especialização")
    public ResponseEntity post(@RequestBody EspecializacaoDTO dto) {
        try {
            Especializacao especializacao = converter(dto);
            especializacao = service.salvar(especializacao);
            return new ResponseEntity(especializacao, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de uma especialização existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EspecializacaoDTO dto) {
        if (!service.getEspecializacaoById(id).isPresent()) {
            return new ResponseEntity("Especialização não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Especializacao especializacao = converter(dto);
            especializacao.setId(id);
            service.salvar(especializacao);
            return ResponseEntity.ok(especializacao);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui uma especialização")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Especializacao> especializacao = service.getEspecializacaoById(id);
        if (!especializacao.isPresent()) {
            return new ResponseEntity("Especialização não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(especializacao.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Especializacao converter(EspecializacaoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Especializacao especializacao = modelMapper.map(dto, Especializacao.class);
        return especializacao;
    }
}
