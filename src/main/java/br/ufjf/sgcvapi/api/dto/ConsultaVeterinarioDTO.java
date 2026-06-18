package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.ConsultaVeterinario;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import jakarta.persistence.ManyToOne;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

public class ConsultaVeterinarioDTO {

    private Long id;
    private Long idVeterinario;
    private Long idConsulta;

    private String nomeVeterinario;

    public static ConsultaVeterinarioDTO create(ConsultaVeterinario consultaVeterinario){
        ModelMapper modelMapper = new ModelMapper();
        ConsultaVeterinarioDTO dto = modelMapper.map(consultaVeterinario, ConsultaVeterinarioDTO.class);
        dto.nomeVeterinario = consultaVeterinario.getVeterinario().getNome();
        return dto;
    }
}
