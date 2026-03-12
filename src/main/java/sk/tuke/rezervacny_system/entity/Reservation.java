package sk.tuke.rezervacny_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@NamedQuery(name = "Reservation.findByStudent", query = "SELECT r FROM Reservation r WHERE r.student = :student")
@NamedQuery(name = "Reservation.findBySlot", query = "SELECT r FROM Reservation r WHERE r.slot = :slot AND r.active = true")
@NamedQuery(name = "Reservation.countBySlot", query = "SELECT COUNT(r) FROM Reservation r WHERE r.slot = :slot AND r.active = true")
@NamedQuery(name = "Reservation.findBySlotAndStudent", query = "SELECT r FROM Reservation r WHERE r.slot = :slot AND r.student = :student")

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private ConsultationSlot slot;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private LocalDateTime createdAt;

    private boolean active = true;

    private String status = "PENDING";



    //k
    public Reservation() {
    }

    public Reservation(Long id, ConsultationSlot slot, User student, LocalDateTime createdAt, Boolean active) {
        this.id = id;
        this.slot = slot;
        this.student = student;
        this.createdAt = createdAt;
        this.active = active;
    }



    //gs
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConsultationSlot getSlot() {
        return slot;
    }

    public void setSlot(ConsultationSlot slot) {
        this.slot = slot;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
