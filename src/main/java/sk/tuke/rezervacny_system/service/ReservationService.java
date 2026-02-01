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
        return reservationRepository.findByStudent(student);
    }
}
