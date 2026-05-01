package br.ufjf.sgcvapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Vacina{

    @Id
    private String numLote;

    private String nome;
    private String descricao;
    private Float valor;
}
