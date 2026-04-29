package br.ufjf.sgcvapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String data_de_nascimento;
    private String sexo;
    private boolean ehCastrado;
    private boolean estaInternado;
    private float peso;

    @ManyToOne
    private Raca raca;

    //@ManyToOne ADICIONAR ESPÉCIE AQUI
    //private Especie
}
