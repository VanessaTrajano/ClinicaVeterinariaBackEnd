package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Raca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RacaDTO {
    private Long id;
    private String nome;
    private Long idEspecie;

    public static RacaDTO create(Raca raca){
        ModelMapper modelMapper = new ModelMapper();
        RacaDTO dto = modelMapper.map(raca, RacaDTO.class);
        return dto;
    }
}
