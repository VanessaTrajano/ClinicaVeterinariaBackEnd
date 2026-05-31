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
    // essa classe precisa de avaliação posterior, ainda há dúvida sobre a estruturação
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Servico servico;

    @OneToOne
    private Disponibilidade disponibilidade;

    @ManyToOne
    private Pet pet;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Consulta consulta;
}
