package sk.tuke.rezervacny_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import sk.tuke.rezervacny_system.entity.ConsultationSlot;
import sk.tuke.rezervacny_system.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationSlotRepository extends JpaRepository<ConsultationSlot, Long> {
    List<ConsultationSlot> findAvailableFuture(@Param("now") LocalDateTime now); //zobrazovanie neobsadenych terminov
    List<ConsultationSlot> findByTeacher(@Param("teacher") User teacher, @Param("now") LocalDateTime now); //zobrazenie iba jeho aktivnych slotov pre ucitela - kvoli tomu ze ak nejaky zamietne a zmeni sa na false aby ho nezobrazovalo
}
