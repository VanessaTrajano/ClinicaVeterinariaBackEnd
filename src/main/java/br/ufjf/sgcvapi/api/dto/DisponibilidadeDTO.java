package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Disponibilidade;
import br.ufjf.sgcvapi.model.entity.Veterinario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeDTO {
    private Long id;
    private String data;
    private String horario;
    private Veterinario veterinario;

    public static DisponibilidadeDTO create(Disponibilidade disponibilidade){
        ModelMapper modelMapper = new ModelMapper();
        DisponibilidadeDTO dto = modelMapper.map(disponibilidade, DisponibilidadeDTO.class);
        return dto;
    }
}
