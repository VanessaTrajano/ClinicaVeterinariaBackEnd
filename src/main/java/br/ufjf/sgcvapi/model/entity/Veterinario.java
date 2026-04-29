package br.ufjf.sgcvapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Veterinario extends Pessoa{

    private String crmv;

    //@ManyToMany
    //private List<Especializacao> especializacao;
}
