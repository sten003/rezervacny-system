package sk.tuke.rezervacny_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.rezervacny_system.entity.ConsultationSlot;
import sk.tuke.rezervacny_system.entity.Reservation;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.ConsultationSlotRepository;
import sk.tuke.rezervacny_system.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConsultationSlotRepository slotRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ConsultationSlotRepository slotRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
    }

    public Reservation reserve(Long slotId, User student) {
        ConsultationSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot neexistuje"));

        Reservation reservation = new Reservation(null, slot, student, LocalDateTime.now(), true);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsForStudent(User student) {
        return reservationRepository.findByStudent(student, LocalDateTime.now());
    }

    public void approveReservation(Long reservationId, User teacher) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Chyba"));

        //overenie ci rezervacia patri ucitelovi
        if (!reservation.getSlot().getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Nemáte oprávnenie na schválenie tejto rezervácie!");
        }

        reservation.setStatus("APPROVED");
        reservationRepository.save(reservation);
    }

    public void rejectReservation(Long reservationId, User teacher) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Chyba"));

        if (!reservation.getSlot().getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Nemáte oprávnenie na zamietnutie tejto rezervácie!");
        }

        //neodstranime ju z databazy aby student videl ze je zamitnuta
        reservation.setStatus("REJECTED");
        reservation.setActive(false);
        reservationRepository.save(reservation);

        ConsultationSlot slot = reservation.getSlot();
        slot.setActive(false);
        slotRepository.save(slot);
    }
}
