package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.VeterinarioDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import br.ufjf.sgcvapi.model.entity.Endereco;
import br.ufjf.sgcvapi.service.VeterinarioService;
import br.ufjf.sgcvapi.service.EnderecoService;
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
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Veterinários", description = "Operações relacionadas a veterinários")
public class VeterinarioController {

    private final VeterinarioService service;
    private final EnderecoService enderecoService;

    @GetMapping()
    @Operation(summary = "Lista todos os veterinários")
    public ResponseEntity get() {
        List<Veterinario> veterinarios = service.getVeterinarios();
        return ResponseEntity.ok(veterinarios.stream().map(VeterinarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um veterinário pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinario não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario.map(VeterinarioDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo veterinário")
    public ResponseEntity post(@RequestBody VeterinarioDTO dto) {
        try {
            Veterinario veterinario = converter(dto);
            veterinario = service.salvar(veterinario);
            return new ResponseEntity(veterinario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um veterinário existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VeterinarioDTO dto) {
        if (!service.getVeterinarioById(id).isPresent()) {
            return new ResponseEntity("Veterinario não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Veterinario veterinario = converter(dto);
            veterinario.setId(id);
            service.salvar(veterinario);
            return ResponseEntity.ok(veterinario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um veterinário")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinario não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(veterinario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Veterinario converter(VeterinarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Veterinario veterinario = modelMapper.map(dto, Veterinario.class);
        Optional<Endereco> endereco = enderecoService.getEnderecoById(dto.getIdEndereco());
        if(!endereco.isPresent()) {
            throw new RegraNegocioException("Endereço não encontrado");
        }
        veterinario.setEndereco(endereco.get());
        return veterinario;
    }
}
