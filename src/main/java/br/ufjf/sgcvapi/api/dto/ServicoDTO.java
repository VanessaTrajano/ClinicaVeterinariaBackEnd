package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Servico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private float valor;
    private boolean vacinaEhObrigatorio;
    private boolean medicamentoEhObrigatorio;

    public static ServicoDTO create(Servico servico){
        ModelMapper modelMapper = new ModelMapper();
        ServicoDTO dto = modelMapper.map(servico, ServicoDTO.class);
        return dto;
    }
}
