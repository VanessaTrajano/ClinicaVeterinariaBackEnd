package br.ufjf.sgcvapi.api.dto;

import br.ufjf.sgcvapi.model.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
    private Long id;
    private String nome;
    private String dataDeNascimento;
    private String sexo;
    private boolean ehCastrado;
    private boolean estaInternado;
    private float peso;
    private Long idDono;
    private Long idRaca;

    private String nomeDono;
    private String nomeRaca;
    private Long idEspecie;
    private String nomeEspecie;

    public static PetDTO create(Pet pet){
        ModelMapper modelMapper = new ModelMapper();
        PetDTO dto = modelMapper.map(pet, PetDTO.class);
        dto.idEspecie = pet.getRaca().getEspecie().getId();
        dto.nomeEspecie = pet.getRaca().getEspecie().getNome();
        dto.nomeDono = pet.getDono().getNome();
        dto.nomeRaca = pet.getRaca().getNome();
        return dto;
    }
}
