package sk.tuke.rezervacny_system.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.rezervacny_system.entity.Role;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.UserRepository;
import sk.tuke.rezervacny_system.service.ConsultationService;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final ConsultationService consultationService;
    private final UserRepository userRepository;

    public TeacherController(ConsultationService consultationService, UserRepository userRepository) {
        this.consultationService = consultationService;
        this.userRepository = userRepository;
    }

    //zakladne ui
    @GetMapping("/overview")
    public String overview(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");

        //kontrola ci je pouzivatel prihlaseny a ma spravnu rolu
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        User teacher = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("ucitel nenajdeny"));

        model.addAttribute("slots", consultationService.getSlotsForTeacher(teacher));
        model.addAttribute("teacherName", teacher.getFullName());

        return "teacher/overview";
    }

    //vytvorenie terminu
    @PostMapping("/create")
    public String createSlot(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String description) {

        User teacher = userRepository.findByUsername("teacher")
                .orElseThrow(() -> new RuntimeException("profil nenajdeny"));
        consultationService.createSlot(teacher, start, end, description);

        return "redirect:/teacher/overview";
    }
}
