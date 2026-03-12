package sk.tuke.rezervacny_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.rezervacny_system.entity.ConsultationSlot;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.ConsultationSlotRepository;

import java.time.LocalDateTime;
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

    //necitanie slotov podla ucitela
    public List<ConsultationSlot> getSlotsForTeacher(User teacher) {
        return slotRepository.findByTeacher(teacher, LocalDateTime.now());
    }

    //nova metoda kvôli nezobrazovaniuu obsadenych slotov
    public List<ConsultationSlot> getActiveFutureSlots() {
        return slotRepository.findAvailableFuture(LocalDateTime.now());
    }
}
