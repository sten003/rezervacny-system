package sk.tuke.rezervacny_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.rezervacny_system.entity.ConsultationSlot;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.ConsultationSlotRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Transactional
public class ConsultationService {

    private final ConsultationSlotRepository slotRepository;

    public ConsultationService(ConsultationSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public ConsultationSlot createSlot(User teacher,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       String description) {
        ConsultationSlot slot = new ConsultationSlot(null, teacher, start, end, description, true);
        return slotRepository.save(slot);
    }

    public void createRepeatingSlots(User teacher,
                                     DayOfWeek dayOfWeek,
                                     LocalTime startTime,
                                     LocalTime endTime,
                                     String description,
                                     int repeat) {
        LocalDate nextDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek));

        for (int i = 0; i < repeat; i++) {
            LocalDateTime slotStartTime = LocalDateTime.of(nextDate, startTime);
            LocalDateTime slotEndTime = LocalDateTime.of(nextDate, endTime);

            ConsultationSlot slot = new ConsultationSlot();
            slot.setTeacher(teacher);
            slot.setStartTime(slotStartTime);
            slot.setEndTime(slotEndTime);
            slot.setDescription(description);
            slot.setActive(true);

            slotRepository.save(slot);

            nextDate = nextDate.plusWeeks(1);
        }
    }

    //necitanie slotov podla ucitela
    public List<ConsultationSlot> getSlotsForTeacher(User teacher) {
        return slotRepository.findByTeacher(teacher, LocalDateTime.now());
    }

    //nova metoda kvôli nezobrazovaniuu obsadenych slotov
    public List<ConsultationSlot> getActiveFutureSlots() {
        return slotRepository.findAvailableFuture(LocalDateTime.now());
    }
}
