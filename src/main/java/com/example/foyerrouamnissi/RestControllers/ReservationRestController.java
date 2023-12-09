package com.example.foyerrouamnissi.RestControllers;

import com.example.foyerrouamnissi.Services.Reservation.IReservationService;

import com.example.foyerrouamnissi.DAO.Entities.Reservation;
import com.example.foyerrouamnissi.Services.Reservation.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReservationRestController {
    @Autowired
    IReservationService reservationService;
    @PostMapping("/addReservation")
    Reservation addReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }
    @PostMapping("/ajouterReservation")
    Map<String, Object> ajouterReservation(@RequestBody Reservation reservation) {
        return reservationService.ajouterReservation(reservation);
    }
    @PutMapping("/updateReservation")
    Reservation updateReservation(@RequestBody Reservation reservation) {
        return reservationService.updateReservation(reservation);
    }
    /************** Valider une reservation ou refuser *************/

    @PutMapping("/estValide/{id}")
    public Map<String, Object> estValide(@PathVariable Integer id) {
        Map<String, Object> response = reservationService.estValide(id);

        return response;

    }

    @PutMapping("/nonValide/{id}")
    void nonValide(@PathVariable Integer id) {
        reservationService.nonValide(id);
    }

    @GetMapping("/getAllReservations")
    List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
    @GetMapping("/getMesReservations/{cinEtudiant}")
    Map<String, Object>getMesReservations(@PathVariable Long cinEtudiant) {
        return reservationService.getMesReservations(cinEtudiant);
    }
    @GetMapping("/getByIdReservation/{idReservation}")
    Optional<Reservation> getByIdReservation(@PathVariable Integer idReservation) {
        return reservationService.getByIdReservation(idReservation);
    }
    @DeleteMapping("/deleteReservation/{id}")
    void deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
    }
}