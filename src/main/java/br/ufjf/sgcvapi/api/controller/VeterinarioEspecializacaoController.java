package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.VeterinarioEspecializacaoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Especializacao;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import br.ufjf.sgcvapi.model.entity.VeterinarioEspecializacao;
import br.ufjf.sgcvapi.service.EspecializacaoService;
import br.ufjf.sgcvapi.service.VeterinarioEspecializacaoService;
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
@RequestMapping("/api/v1/veterinarioEspecializacoes")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "VeterinarioEspecializacoes", description = "Operações relacionadas à ligação de veterinários e especializações já cadastradas")
public class VeterinarioEspecializacaoController {

    private final VeterinarioEspecializacaoService service;
    private final VeterinarioService veterinarioService;
    private final EspecializacaoService especializacaoService;

    @GetMapping()
    public ResponseEntity get() {
        List<VeterinarioEspecializacao> veterinarioEspecializacoes = service.getVeterinarioEspecializacoes();
        return ResponseEntity.ok(veterinarioEspecializacoes.stream().map(VeterinarioEspecializacaoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista todos os veterinarioEspecializacoes")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<VeterinarioEspecializacao> veterinarioEspecializacao = service.getVeterinarioEspecializacaoById(id);
        if (!veterinarioEspecializacao.isPresent()) {
            return new ResponseEntity("VeterinarioEspecializacao não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinarioEspecializacao.map(VeterinarioEspecializacaoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo veterinarioEspecializacao")
    public ResponseEntity post(@RequestBody VeterinarioEspecializacaoDTO dto) {
        try {
            VeterinarioEspecializacao veterinarioEspecializacao = converter(dto);
            veterinarioEspecializacao = service.salvar(veterinarioEspecializacao);
            return new ResponseEntity(veterinarioEspecializacao, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um veterinarioEspecializacao existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VeterinarioEspecializacaoDTO dto) {
        if (!service.getVeterinarioEspecializacaoById(id).isPresent()) {
            return new ResponseEntity("VeterinarioEspecializacao não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            VeterinarioEspecializacao veterinarioEspecializacao = converter(dto);
            veterinarioEspecializacao.setId(id);
            service.salvar(veterinarioEspecializacao);
            return ResponseEntity.ok(veterinarioEspecializacao);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um veterinarioEspecializacao")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<VeterinarioEspecializacao> veterinarioEspecializacao = service.getVeterinarioEspecializacaoById(id);
        if (!veterinarioEspecializacao.isPresent()) {
            return new ResponseEntity("VeterinarioEspecializacao não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(veterinarioEspecializacao.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public VeterinarioEspecializacao converter(VeterinarioEspecializacaoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        VeterinarioEspecializacao veterinarioEspecializacao = modelMapper.map(dto, VeterinarioEspecializacao.class);
        Optional<Veterinario> veterinario = veterinarioService.getVeterinarioById(dto.getIdVeterinario());
        if(!veterinario.isPresent()) {
            throw new RegraNegocioException("Veterinário não encontrado");
        }
        veterinarioEspecializacao.setVeterinario(veterinario.get());
        Optional<Especializacao> especializacao = especializacaoService.getEspecializacaoById(dto.getIdEspecializacao());
        if(!especializacao.isPresent()) {
            throw new RegraNegocioException("Especialização não encontrada");
        }
        veterinarioEspecializacao.setEspecializacao(especializacao.get());
        return veterinarioEspecializacao;
    }
}
