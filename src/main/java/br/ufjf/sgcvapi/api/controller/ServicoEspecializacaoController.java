package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.ServicoEspecializacaoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especializacao;
import br.ufjf.sgcvapi.model.entity.Servico;
import br.ufjf.sgcvapi.model.entity.ServicoEspecializacao;
import br.ufjf.sgcvapi.service.EspecializacaoService;
import br.ufjf.sgcvapi.service.ServicoEspecializacaoService;
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
@RequestMapping("/api/v1/servicoEspecializacoes")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "ServicoEspecializacoes", description = "Operações relacionadas à ligações de serviços e especializações já cadastradas")
public class ServicoEspecializacaoController {

    private final ServicoEspecializacaoService service;
    private final ServicoService servicoService;
    private final EspecializacaoService especializacaoService;

    @GetMapping()
    @Operation(summary = "Lista todos os servicoEspecializacoes")
    public ResponseEntity get() {
        List<ServicoEspecializacao> servicoEspecializacaos = service.getServicoEspecializacaos();
        return ResponseEntity.ok(servicoEspecializacaos.stream().map(ServicoEspecializacaoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um servicoEspecializacao pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ServicoEspecializacao> servicoEspecializacao = service.getServicoEspecializacaoById(id);
        if (!servicoEspecializacao.isPresent()) {
            return new ResponseEntity("ServicoEspecializacao não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(servicoEspecializacao.map(ServicoEspecializacaoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo servicoEspecializacao")
    public ResponseEntity post(@RequestBody ServicoEspecializacaoDTO dto) {
        try {
            ServicoEspecializacao servicoEspecializacao = converter(dto);
            servicoEspecializacao = service.salvar(servicoEspecializacao);
            return new ResponseEntity(servicoEspecializacao, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza dados de um servicoEspecializacao existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ServicoEspecializacaoDTO dto) {
        if (!service.getServicoEspecializacaoById(id).isPresent()) {
            return new ResponseEntity("ServicoEspecializacao não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            ServicoEspecializacao servicoEspecializacao = converter(dto);
            servicoEspecializacao.setId(id);
            service.salvar(servicoEspecializacao);
            return ResponseEntity.ok(servicoEspecializacao);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um servicoEspecializacao")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ServicoEspecializacao> servicoEspecializacao = service.getServicoEspecializacaoById(id);
        if (!servicoEspecializacao.isPresent()) {
            return new ResponseEntity("ServicoEspecializacao não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(servicoEspecializacao.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ServicoEspecializacao converter(ServicoEspecializacaoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ServicoEspecializacao servicoEspecializacao = modelMapper.map(dto, ServicoEspecializacao.class);
        Optional<Servico> servico = servicoService.getServicoById(dto.getIdServico());
        if(!servico.isPresent()) {
            throw new RegraNegocioException("Serviço não encontrada");
        }
        servicoEspecializacao.setServico(servico.get());
        Optional<Especializacao> especializacao = especializacaoService.getEspecializacaoById(dto.getIdEspecializacao());
        if(!especializacao.isPresent()) {
            throw new RegraNegocioException("Especialização não encontrada");
        }
        servicoEspecializacao.setEspecializacao(especializacao.get());
        return servicoEspecializacao;
    }
}
