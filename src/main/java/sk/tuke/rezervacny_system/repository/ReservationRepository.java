package sk.tuke.rezervacny_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tuke.rezervacny_system.entity.Reservation;
import sk.tuke.rezervacny_system.entity.User;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStudent(User student, LocalDateTime now);
    List<Reservation> findBySlotTeacherAndStatus(User teacher, String status);
}
