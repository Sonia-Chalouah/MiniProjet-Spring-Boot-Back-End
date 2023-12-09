package com.example.foyerrouamnissi.Services.Reservation;

import com.example.foyerrouamnissi.DAO.Entities.Reservation;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IReservationService{
    Reservation addReservation(Reservation reservation);
    Map<String, Object> ajouterReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Map<String, Object> getMesReservations(Long cinUser);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(Integer id);
    void nonValide(Integer id);
    Map<String, Object> estValide(Integer id);

    Optional<Reservation> getByIdReservation(Integer idReservation);
}



