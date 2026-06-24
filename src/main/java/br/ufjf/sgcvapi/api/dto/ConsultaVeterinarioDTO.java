package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.ConsultaVeterinario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
