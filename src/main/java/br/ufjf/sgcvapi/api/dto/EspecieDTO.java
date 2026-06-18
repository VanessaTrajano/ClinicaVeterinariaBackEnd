package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Especie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecieDTO {
    private Long id;
    private String nome;

    public static EspecieDTO create(Especie especie){
        ModelMapper modelMapper = new ModelMapper();
        EspecieDTO dto = modelMapper.map(especie, EspecieDTO.class);
        return dto;
    }
}
