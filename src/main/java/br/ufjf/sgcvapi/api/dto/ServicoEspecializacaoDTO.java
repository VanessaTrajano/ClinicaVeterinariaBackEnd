package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Especializacao;
import br.ufjf.sgcvapi.model.entity.Servico;
import br.ufjf.sgcvapi.model.entity.ServicoEspecializacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoEspecializacaoDTO {
    private Long id;
    private Long idServico;
    private Long idEspecializacao;

    public static ServicoEspecializacaoDTO create(ServicoEspecializacao servicoEspecializacao){
        ModelMapper modelMapper = new ModelMapper();
        ServicoEspecializacaoDTO dto = modelMapper.map(servicoEspecializacao, ServicoEspecializacaoDTO.class);
        return dto;
    }
}
