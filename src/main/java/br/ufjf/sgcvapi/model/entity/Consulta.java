package br.ufjf.sgcvapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String diagnostico;
    private String receita;
    private String motivoInternacao;
    private Integer quantDiasInternacao;
    private float valor;

    @ManyToOne
    private Pet pet;
}
