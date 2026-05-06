package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Especializacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecializacaoDTO {
    private Long id;
    private String nome;

    public static EspecializacaoDTO create(Especializacao especializacao){
        ModelMapper modelMapper = new ModelMapper();
        EspecializacaoDTO dto = modelMapper.map(especializacao, EspecializacaoDTO.class);
        return dto;
    }
}
