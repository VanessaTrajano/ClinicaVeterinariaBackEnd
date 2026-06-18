package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.MedicamentoPet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoPetDTO {
    private Long id;
    private Long idAlergia;
    private Long idPet;

    private String nomeAlergia;
    private String nomePet;

    public static MedicamentoPetDTO create(MedicamentoPet medicamentoPet){
        ModelMapper modelMapper = new ModelMapper();
        MedicamentoPetDTO dto = modelMapper.map(medicamentoPet, MedicamentoPetDTO.class);
        dto.nomeAlergia = medicamentoPet.getAlergia().getNome();
        dto.nomePet = medicamentoPet.getPet().getNome();
        return dto;
    }
}
