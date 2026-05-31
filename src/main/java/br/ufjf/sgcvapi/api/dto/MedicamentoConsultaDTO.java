package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Medicamento;
import br.ufjf.sgcvapi.model.entity.MedicamentoConsulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoConsultaDTO {
    private Long id;
    private Medicamento medicamento;
    private Consulta consulta;

    public static MedicamentoConsultaDTO create(MedicamentoConsulta medicamentoConsulta){
        ModelMapper modelMapper = new ModelMapper();
        MedicamentoConsultaDTO dto = modelMapper.map(medicamentoConsulta, MedicamentoConsultaDTO.class);
        return dto;
    }
}
