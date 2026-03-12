package sk.tuke.rezervacny_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "consultation_slots")
@NamedQuery(name = "ConsultationSlot.findByTeacher", query = "SELECT s FROM ConsultationSlot s WHERE s.teacher = :teacher AND s.active = true AND s.startTime > :now ORDER BY s.startTime ASC")
@NamedQuery(name = "ConsultationSlot.findAllActive", query = "SELECT s FROM ConsultationSlot s WHERE s.active = true ORDER BY s.startTime")
//@NamedQuery(name = "ConsultationSlot.findFuture", query = "SELECT s FROM ConsultationSlot s WHERE s.active = true AND s.startTime > :now ORDER BY s.startTime")
@NamedQuery(name = "ConsultationSlot.findAvailableFuture", query = "SELECT s FROM ConsultationSlot s WHERE s.active = true AND s.startTime > :now " +
                "AND NOT EXISTS (SELECT r FROM Reservation r WHERE r.slot = s AND r.active = true) " + "ORDER BY s.startTime")

public class ConsultationSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    //kvoli ucitelskemu rozhraniu
    //prepojenie aby sme sa dalo vypisat ze rezervacia je potvrdena pri konkretnom rezervacnom slote
    @OneToMany(mappedBy = "slot", fetch = FetchType.EAGER)
    private List<Reservation> reservations;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    private boolean active = true;



    //k

    public ConsultationSlot() {
    }

    public ConsultationSlot(Long id, User teacher, LocalDateTime startTime, LocalDateTime endTime, String description, Boolean active) {
        this.id = id;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.active = active;
    }



    //gs

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
