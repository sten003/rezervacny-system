package sk.tuke.rezervacny_system.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultation_slots")
@NamedQuery(name = "ConsultationSlot.findByTeacher", query = "SELECT s FROM ConsultationSlot s WHERE s.teacher = :teacher")
@NamedQuery(name = "ConsultationSlot.findAllActive", query = "SELECT s FROM ConsultationSlot s WHERE s.active = true ORDER BY s.startTime")
@NamedQuery(name = "ConsultationSlot.findFuture", query = "SELECT s FROM ConsultationSlot s WHERE s.active = true AND s.startTime > :now ORDER BY s.startTime")

public class ConsultationSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int maxStudents;

    private String description;

    private boolean active = true;



    //k

    public ConsultationSlot() {
    }

    public ConsultationSlot(Long id, User teacher, LocalDateTime startTime, LocalDateTime endTime, Integer maxStudents, String description, Boolean active) {
        this.id = id;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxStudents = maxStudents;
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

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
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
}
