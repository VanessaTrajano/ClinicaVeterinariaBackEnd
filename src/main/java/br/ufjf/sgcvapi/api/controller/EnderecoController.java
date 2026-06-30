package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.EnderecoDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Endereco;
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
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Endereços", description = "Operações relacionadas a endereços")
public class EnderecoController {

    private final EnderecoService service;

    @GetMapping()
    @Operation(summary = "Lista todos os endereços")
    public ResponseEntity get() {
        List<Endereco> enderecos = service.getEnderecos();
        return ResponseEntity.ok(enderecos.stream().map(EnderecoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta um endereço pelo ID")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Endereco> endereco = service.getEnderecoById(id);
        if (!endereco.isPresent()) {
            return new ResponseEntity("Endereco não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(endereco.map(EnderecoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cadastra um novo endereço")
    public ResponseEntity post(@RequestBody EnderecoDTO dto) {
        try {
            Endereco endereco = converter(dto);
            endereco = service.salvar(endereco);
            return new ResponseEntity(endereco, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um endereço existente")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EnderecoDTO dto) {
        if (!service.getEnderecoById(id).isPresent()) {
            return new ResponseEntity("Endereco não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Endereco endereco = converter(dto);
            endereco.setId(id);
            service.salvar(endereco);
            return ResponseEntity.ok(endereco);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Exclui um endereço")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Endereco> endereco = service.getEnderecoById(id);
        if (!endereco.isPresent()) {
            return new ResponseEntity("Endereco não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(endereco.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Endereco converter(EnderecoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Endereco endereco = modelMapper.map(dto, Endereco.class);
        return endereco;
    }
}
