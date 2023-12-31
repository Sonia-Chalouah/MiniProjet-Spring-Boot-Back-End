package com.example.foyerrouamnissi.DAO.Repositories;

import com.example.foyerrouamnissi.DAO.Entities.Chambre;
import com.example.foyerrouamnissi.DAO.Entities.Foyer;
import com.example.foyerrouamnissi.DAO.Entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChambreRepository  extends JpaRepository<Chambre,Long > {
    // 1- Recherche par numéro de chambre
   // List<Chambre> findByNumeroChambre(Long numeroChambre);
    Chambre findByNumeroChambre(Long numeroChambre);
    List<Chambre> findByNumeroChambreAndTypeC(Long numeroChambre, TypeChambre typeC);
    List<Chambre> findByTypeC(TypeChambre typeC);

    // 2- Recherche par type de chambre
    //List<Chambre> findByTypeChambre(TypeChambre typeChambre);

    //3- Recherche des chambres par bloc
    //List<Chambre> findByBloc(Chambre bloc);

    // 4- Recherche des chambres par bloc et type de chambre
    // List<Chambre> findByBlocAndTypeChambre(Chambre bloc, TypeChambre typeChambre);

    // 5- Recherche des chambres par numéro de chambre et type de chambre

    List<Chambre> findByBloc_NomBloc(String nomBloc);
    long countByTypeCAndBloc_IdBloc(TypeChambre type, long idBloc);


/*
    @Query("SELECT c FROM Chambre c WHERE c.bloc.foyer = :foyer AND c.typeC = :type AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.TypeChambre= c AND r.anneeUniversitaire = :currentYear)")
    List<Chambre> findNonReservedRoomsByFoyerAndType(@Param("foyer") Foyer foyer, @Param("type") TypeChambre type, @Param("currentYear") String currentYear);

*/
    @Query("SELECT c.typeC, COUNT(c) FROM Chambre c GROUP BY c.typeC")
    List<Object[]> countChambresByType();

//changed
@Query("SELECT c FROM Chambre c " +
        "JOIN c.bloc b " +
        "JOIN b.foyer f " +
        "JOIN f.universite u " +
        "WHERE c.typeC= :type " +
        "AND c.capaciteChambre > 0 " +
        "AND u.idUniversite = :idUniversite " +
        "ORDER BY c.idChambre ASC")
Chambre findChambreDisponible(@Param("type") TypeChambre type, @Param("idUniversite") Long idUniversite);

}
