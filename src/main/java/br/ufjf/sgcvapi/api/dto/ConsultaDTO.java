package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Consulta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {

    private Long id;
    private String status;
    private String diagnostico;
    private String receita;
    private String motivoInternacao;
    private Integer quantDiasInternacao;
    private float valor;
    private Long idPet;

    private String nomePet;
    private String nomeCliente;
    private Long idCliente;

    public static ConsultaDTO create(Consulta consulta){
        ModelMapper modelMapper = new ModelMapper();
        ConsultaDTO dto = modelMapper.map(consulta, ConsultaDTO.class);
        dto.nomePet = consulta.getPet().getNome();
        dto.idCliente = consulta.getPet().getDono().getId();
        dto.nomeCliente = consulta.getPet().getDono().getNome();
        return dto;
    }
}
