package br.ufjf.sgcvapi.api.controller;

import br.ufjf.sgcvapi.api.dto.VeterinarioDTO;
import br.ufjf.sgcvapi.exception.RegraNegocioException;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import br.ufjf.sgcvapi.model.entity.Endereco;
import br.ufjf.sgcvapi.service.VeterinarioService;
import br.ufjf.sgcvapi.service.EnderecoService;
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
public class VeterinarioController {

    private final VeterinarioService service;
    private final EnderecoService enderecoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Veterinario> veterinarios = service.getVeterinarios();
        return ResponseEntity.ok(veterinarios.stream().map(VeterinarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinario não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(veterinario.map(VeterinarioDTO::create));
    }

    @PostMapping()
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
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody VeterinarioDTO dto) {
        if (!service.getVeterinarioById(id).isPresent()) {
            return new ResponseEntity("Veterinario não encontrada", HttpStatus.NOT_FOUND);
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
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Veterinario> veterinario = service.getVeterinarioById(id);
        if (!veterinario.isPresent()) {
            return new ResponseEntity("Veterinario não encontrada", HttpStatus.NOT_FOUND);
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
            throw new RegraNegocioException("Endereço não encontrada");
        }
        veterinario.setEndereco(endereco.get());
        return veterinario;
    }
}
