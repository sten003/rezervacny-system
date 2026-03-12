package sk.tuke.rezervacny_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.rezervacny_system.repository.ConsultationSlotRepository;
import sk.tuke.rezervacny_system.repository.ReservationRepository;

@Service
public class DatabaseCleanupService {
    private final ReservationRepository reservationRepository;
    private final ConsultationSlotRepository consultationSlotRepository;

    public DatabaseCleanupService(ReservationRepository reservationRepository, ConsultationSlotRepository consultationSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.consultationSlotRepository = consultationSlotRepository;
    }

    @Transactional
    public void deleteAllSlotsAndReservations() {
        reservationRepository.deleteAll();
        consultationSlotRepository.deleteAll();
    }
}