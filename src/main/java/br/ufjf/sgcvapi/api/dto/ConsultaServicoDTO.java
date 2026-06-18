package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.ConsultaServico;
import br.ufjf.sgcvapi.model.entity.Servico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaServicoDTO {

    private Long id;
    private Integer quantServicos;
    private Long idConsulta;
    private Long idServico;

    public static ConsultaServicoDTO create(ConsultaServico consultaServico){
        ModelMapper modelMapper = new ModelMapper();
        ConsultaServicoDTO dto = modelMapper.map(consultaServico, ConsultaServicoDTO.class);
        return dto;
    }

}
