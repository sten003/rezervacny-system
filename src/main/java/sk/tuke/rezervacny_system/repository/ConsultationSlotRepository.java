package sk.tuke.rezervacny_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import sk.tuke.rezervacny_system.entity.ConsultationSlot;
import sk.tuke.rezervacny_system.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationSlotRepository extends JpaRepository<ConsultationSlot, Long> {
    List<ConsultationSlot> findByTeacher(User teacher); //sloty podla ucitela
    //List<ConsultationSlot> findByActiveTrueAndStartTimeAfter(LocalDateTime dateTime); //aktivne sloty po aktualnom case - kvoli nezobrazovaniu starych terminov
    List<ConsultationSlot> findAvailableFuture(@Param("now") LocalDateTime now); //zobrazovanie neobsadenych terminov
}
