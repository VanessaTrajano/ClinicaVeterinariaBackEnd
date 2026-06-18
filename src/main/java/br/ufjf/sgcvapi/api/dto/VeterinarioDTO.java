package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Veterinario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioDTO {

    private Long id;
    private String crmv;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private Long idEndereco;

    public static VeterinarioDTO create(Veterinario veterinario) {
        ModelMapper modelMapper = new ModelMapper();
        VeterinarioDTO dto = modelMapper.map(veterinario, VeterinarioDTO.class);
        return dto;
    }
}
