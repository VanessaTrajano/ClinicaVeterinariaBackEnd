package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDTO {
    private Long id;
    private Long idServico;
    private Long idDisponibilidade;
    private Long idPet;
    private Long idCliente;
    private Long idConsulta;

    public static AgendamentoDTO create(Agendamento agendamento){
        ModelMapper modelMapper = new ModelMapper();
        AgendamentoDTO dto = modelMapper.map(agendamento, AgendamentoDTO.class);
        return dto;
    }
}
