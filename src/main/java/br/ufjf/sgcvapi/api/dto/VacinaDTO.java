package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Vacina;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacinaDTO {
    private Long id;
    private String numLote;
    private String nome;
    private String descricao;
    private Float valor;

    public static VacinaDTO create(Vacina vacina){
        ModelMapper modelMapper = new ModelMapper();
        VacinaDTO dto = modelMapper.map(vacina, VacinaDTO.class);
        return dto;
    }
}
