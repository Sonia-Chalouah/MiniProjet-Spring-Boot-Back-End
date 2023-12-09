package com.example.foyerrouamnissi.DAO.Entities;

import com.example.foyerrouamnissi.DAO.Entities.Etudiant;
import com.example.foyerrouamnissi.DAO.Entities.TypeChambre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames= false, exclude = {"idReservation", "etudiants"})
@JsonIgnoreProperties("etudiants")
public class Reservation  implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idReservation" )
    private Integer idReservation;
    @Column(name="numReservation")
    private String numReservation;
    @Column(name="cinEtudiant")
    private Long cinEtudiant;

    @Temporal (TemporalType.DATE)
    private Integer anneeUniversitaire;
    @Column(name = "estValide")
    private boolean estValide;
    @Enumerated(EnumType.STRING)
    private com.example.foyerrouamnissi.DAO.Entities.TypeChambre TypeChambre ;

    @ManyToMany(mappedBy = "reservations")
    private Set<Etudiant> etudiants;

    public boolean getEstValide() {
        return estValide;
    }
}
