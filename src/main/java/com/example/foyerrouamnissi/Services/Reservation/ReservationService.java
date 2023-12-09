package com.example.foyerrouamnissi.Services.Reservation;

import com.example.foyerrouamnissi.DAO.Repositories.*;
import com.example.foyerrouamnissi.DAO.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReservationService implements IReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ChambreRepository chambreRepository;
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    FoyerRepository foyerRepository;
    @Autowired
    BlocRepository blocRepository;
    public static final String ACCOUNT_SID = "AC2eb2c8a866ca28cc41a4c86e365022d4";
    public static final String AUTH_TOKEN = "7e4d1f853a4e48a36a67a2bb4963948a";
    /*************** Add Reservation ************************/
    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /****************************Get All Reservations*******************/

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    @Override
    public Map<String, Object> getMesReservations(Long cinUser) {
        Map<String, Object> response = new HashMap<>();
        List<Reservation> reservations = reservationRepository.findAllByCinEtudiant(cinUser);
        if (reservations.isEmpty()){
            response.put("message", "Vous avez aucune reservation ");
            return response;
        }
        response.put("message", "Voici toutes vos réservations.");
        response.put("reservations", reservations);
        return response;
    }
    /****************************Update Reservation*******************/
    @Override
    public Reservation updateReservation(Reservation reservation) {
        if (reservationRepository.existsById(reservation.getIdReservation())) {
            return reservationRepository.save(reservation);
        }
        return null;
    }
    /****************************delete Reservation*******************/
    @Override
    public void deleteReservation(Integer id) { reservationRepository.deleteById(id); }


    /***************Add V 3 Advances  ************************/
    @Override
    public Map<String, Object> ajouterReservation(Reservation reservation) {
        System.out.println("*****************ajouterReservation**************");
        Map<String, Object> response = new HashMap<>();
        // Vérifier si l'étudiant avec le CIN existe
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByCin(reservation.getCinEtudiant());
        if (etudiantOpt.isEmpty()) {
            // L'étudiant n'existe pas, renvoyer un message d'erreur
            response.put("message", "Étudiant non trouvé avec le CIN : " + reservation.getCinEtudiant());
            return response;
        }else{

            // L'étudiant existe, continuez avec la réservation
            Etudiant etudiantConnecte = etudiantOpt.get();

            // Obtenez l'année actuelle
            int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);

            // Vérifier s'il y a déjà une réservation pour l'étudiant cette année
            Optional<Reservation> reservationExistante = reservationRepository.findByCinEtudiantAndAnneeUniversitaire(
                    etudiantConnecte.getCin(), anneeActuelle);

            if (reservationExistante.isPresent()) {
                // Une réservation existe déjà pour cet étudiant cette année, renvoyer un message d'erreur
                response.put("message", "Une réservation existe déjà pour l'étudiant avec le CIN : " +
                        etudiantConnecte.getCin() + " pour l'année universitaire : " + anneeActuelle);
                return response;
            }

            // L'étudiant n'a pas encore réservé cette année, continuez avec la réservation

            /***ATTRIBUT**/
            reservation.setEstValide(false);
            reservation.setNumReservation("Pas Encore");

            // Définissez l'attribut
            reservation.setAnneeUniversitaire(anneeActuelle);
            /***ATTRIBUT**/
            // Associer la réservation à l'étudiant connecté
            reservation.setEtudiants(Set.of(etudiantConnecte));

            // Associer l'étudiant connecté à la réservation
            Set<Reservation> reservationsEtudiant = etudiantConnecte.getReservations();
            if (reservationsEtudiant == null) {
                reservationsEtudiant = new HashSet<>();
            }
            reservationsEtudiant.add(reservation);
            etudiantConnecte.setReservations(reservationsEtudiant);

            // Ajouter la réservation
            Reservation nouvelleReservation = reservationRepository.save(reservation);

            response.put("message", "Réservation ajoutée avec succès");
            response.put("reservation", nouvelleReservation);
            return response;
        }
    }

    @Override
    public void nonValide(Integer id) {
        Reservation r= reservationRepository.getReferenceById(id);
        r.setEstValide(false);
        reservationRepository.save(r);
    }

    @Override
    public Map<String, Object> estValide(Integer idReservation){
        System.out.println("*****************************EstValide******************************");
        Map<String, Object> response = new HashMap<>();

        // Vérifier si la réservation avec l'ID spécifié existe
        Optional<Reservation> reservationOpt = reservationRepository.findById(idReservation);
        if (reservationOpt.isEmpty()) {
            response.put("message", "Aucune réservation trouvée avec l'ID : " + idReservation);
            return response;
        }
        // La réservation existe, continuer le processus de validation
        Reservation reservation = reservationOpt.get();
        //Vérifier si la réservation a déjà une chambre attribuée
        if (reservation.getEstValide()) {
            response.put("message", "La réservation a déjà une chambre attribuée.");
            response.put("admin", "yes");
            return response;
        }
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByCin(reservation.getCinEtudiant());
        Etudiant etudiantConnecte = etudiantOpt.get();
        // Récupérer une chambre disponible en fonction de capacite
        Chambre chambreDisponible = chambreRepository.findChambreDisponible(reservation.getTypeChambre(),etudiantConnecte.getUniversite().getIdUniversite());

        if (chambreDisponible != null) {

        /* Vérifier si la réservation a déjà une chambre attribuée
        if (chambreDisponible.getReservations() != null) {
            response.put("message", "La réservation a déjà une chambre attribuée.");
            return response;
        }*/

            // Générer le numéro de réservation
            String numReservation = genererNumeroReservation(reservation,chambreDisponible);
            System.out.println("numReservation    : " + numReservation);
            reservation.setNumReservation(numReservation);

            // Mise à jour de la capacité de la chambre
            chambreDisponible.setCapaciteChambre(chambreDisponible.getCapaciteChambre() - 1);

            // Si une chambre du type demandé existe, valider la réservation
            reservation.setEstValide(true);

            // Ajouter la relation entre la chambre et la réservation
            chambreDisponible.getReservations().add(reservation);
            reservation.setTypeChambre(chambreDisponible.getTypeC());

            // Enregistrer les modifications
            chambreRepository.save(chambreDisponible);
            reservationRepository.save(reservation);

            response.put("message", "Réservation validée avec succès.");
            response.put("chambre", reservation.getNumReservation());
            response.put("estValide", reservation.getEstValide());
        } else {
            response.put("message", "Aucune chambre disponible.");
        }

        return response;
    }

    private String genererNumeroReservation(Reservation reservation, Chambre chambre) {
        //    Chambre chambre =  chambreRepository.findChambreDisponible(reservation.getTypeChambre());

        System.out.println("nomFoyer ************"+chambre.getBloc().getFoyer().getNomFoyer());
        String nomFoyer=chambre.getBloc().getFoyer().getNomFoyer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String anneeUniversitaire = sdf.format(new Date());

        return chambre.getNumeroChambre() + "-" + chambre.getBloc().getNomBloc()+"-"+nomFoyer+ "-" + anneeUniversitaire;
    }


    @Override
    public Optional<Reservation> getByIdReservation(Integer idReservation) {
        if (reservationRepository.existsById(idReservation)) {
            return  reservationRepository.findById(idReservation);
        }
        return null;
    }

}
