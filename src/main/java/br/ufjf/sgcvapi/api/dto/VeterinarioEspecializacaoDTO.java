package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.VeterinarioEspecializacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioEspecializacaoDTO {

    private Long id;
    private Long idVeterinario;
    private Long idEspecializacao;

    private String nomeVeterinario;
    private String nomeEspecializacao;

    public static VeterinarioEspecializacaoDTO create(VeterinarioEspecializacao veterinarioEspecializacao){
        ModelMapper modelMapper = new ModelMapper();
        VeterinarioEspecializacaoDTO dto = modelMapper.map(veterinarioEspecializacao, VeterinarioEspecializacaoDTO.class);
        dto.nomeEspecializacao = veterinarioEspecializacao.getEspecializacao().getNome();
        dto.nomeVeterinario = veterinarioEspecializacao.getVeterinario().getNome();
        return dto;
    }
}
