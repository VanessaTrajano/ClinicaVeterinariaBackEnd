package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Medicamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String ium;
    private float valor;

    public static MedicamentoDTO create(Medicamento medicamento){
        ModelMapper modelMapper = new ModelMapper();
        MedicamentoDTO dto = modelMapper.map(medicamento, MedicamentoDTO.class);
        return dto;
    }
}
