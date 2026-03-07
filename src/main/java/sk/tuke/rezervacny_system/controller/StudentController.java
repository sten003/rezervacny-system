package sk.tuke.rezervacny_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.rezervacny_system.entity.Role;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.UserRepository;
import sk.tuke.rezervacny_system.service.ConsultationService;
import sk.tuke.rezervacny_system.service.ReservationService;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final ConsultationService consultationService;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

    public StudentController(ConsultationService consultationService,
                             ReservationService reservationService,
                             UserRepository userRepository) {
        this.consultationService = consultationService;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/overview")
    public String overview(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null || loggedUser.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        User student = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("student nenajdeny"));

        model.addAttribute("availableSlots", consultationService.getActiveFutureSlots());
        model.addAttribute("myReservations", reservationService.getReservationsForStudent(student));
        model.addAttribute("studentName", student.getFullName());

        return "student/overview";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam Long slotId, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null || loggedUser.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        User student = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("student nenajdeny"));

        try {
            reservationService.reserve(slotId, student);
        }
        catch (Exception e) {
            System.err.println("Chyba pri rezervácii: " + e.getMessage());
        }

        return "redirect:/student/overview";
    }
}
