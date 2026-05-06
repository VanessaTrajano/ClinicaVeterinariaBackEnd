package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Consulta;
import br.ufjf.sgcvapi.model.entity.Vacina;
import br.ufjf.sgcvapi.model.entity.VacinaConsulta;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacinaConsultaDTO {
    private Long id;
    private Long idVacina;
    private Long idConsulta;

    private String nomeVacina;

    public static VacinaConsultaDTO create(VacinaConsulta vacinaConsulta){
        ModelMapper modelMapper = new ModelMapper();
        VacinaConsultaDTO dto = modelMapper.map(vacinaConsulta, VacinaConsultaDTO.class);
        dto.nomeVacina = vacinaConsulta.getVacina().getNome();
        return dto;
    }
}
