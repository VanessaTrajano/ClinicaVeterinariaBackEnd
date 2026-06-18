package br.ufjf.sgcvapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean retorno;

    @ManyToOne
    private Pet pet;

    @ManyToOne
    private Cliente cliente;

    @OneToOne
    private Disponibilidade disponibilidade;

    //FALTA INTERLIGAR A CONSULTA
}
