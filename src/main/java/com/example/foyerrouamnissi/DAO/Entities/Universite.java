package com.example.foyerrouamnissi.DAO.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@Entity
@Table(name="Universite")
public class Universite  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUniversite;
    @Column(name="nomUniversite")
    private  String nomUniversite;
    @Column(name="adresse")
    private String  adresse;



    @OneToOne(cascade = CascadeType.ALL)
    private Foyer foyer;
//changed
    @OneToMany(cascade = CascadeType.ALL, mappedBy="universite")
    private Set<Etudiant> etudiants ;


}