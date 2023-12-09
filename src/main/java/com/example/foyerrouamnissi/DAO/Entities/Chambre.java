package com.example.foyerrouamnissi.DAO.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.Serializable;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Chambre")



public class Chambre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idChambre")

    private long idChambre;

    @Column(name="numeroChambre" ,unique = true)

    private  long numeroChambre;
    @Column(name="TypeChambre")

    @Enumerated(EnumType.STRING)
    private TypeChambre typeC;
    //changed
    @Column(name="capaciteChambre")
    private Integer capaciteChambre;



    @ManyToOne
    @JoinColumn(name = "id_bloc")
    private Bloc bloc;

    @OneToMany
    private Set<Reservation> Reservations;


    public Long getBlocId() {
        return bloc != null ? bloc.getIdBloc() : null;
    }
}
