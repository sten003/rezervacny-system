package sk.tuke.rezervacny_system.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.rezervacny_system.entity.Role;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.ReservationRepository;
import sk.tuke.rezervacny_system.repository.UserRepository;
import sk.tuke.rezervacny_system.service.ConsultationService;
import sk.tuke.rezervacny_system.service.ReservationService;
import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final ConsultationService consultationService;
    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    public TeacherController(ConsultationService consultationService, UserRepository userRepository, ReservationService reservationService, ReservationRepository reservationRepository) {
        this.consultationService = consultationService;
        this.userRepository = userRepository;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
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
        //model.addAttribute("pendingReservations", reservationRepository.findBySlotTeacherAndStatus(teacher, "PENDING"));

        return "teacher/overview";
    }

    //vytvorenie terminu
    @PostMapping("/create")
    public String createSlot(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String description,
            HttpSession session) {

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        User teacher = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("ucitel nenajdeny"));

        consultationService.createSlot(teacher, start, end, description);

        return "redirect:/teacher/create-page?success=single";
    }

    //stranka na vytvaranie terminov
    @GetMapping("/create-page")
    public String createPage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");

        //kontrola ci je pouzivatel prihlaseny a ma spravnu rolu
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        User teacher = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("ucitel nenajdeny"));

        model.addAttribute("teacherName", teacher.getFullName());

        return "teacher/create-page";
    }

    //stranka na schvalovanie terminov
    @GetMapping("/approvals")
    public String approvals(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");

        //kontrola ci je pouzivatel prihlaseny a ma spravnu rolu
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        User teacher = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("ucitel nenajdeny"));

        model.addAttribute("teacherName", teacher.getFullName());
        model.addAttribute("pendingReservations", reservationRepository.findBySlotTeacherAndStatus(teacher, "PENDING"));

        return "teacher/approvals";
    }

    //vytvorenie opakovanych terminov
    @PostMapping("/create-repeating-slots")
    public String createRepeatingSlots(
            @RequestParam("dayOfWeek") String dayOfWeekStr,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            @RequestParam String description,
            @RequestParam("weeks") int weeks,
            HttpSession session) {

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        User teacher = userRepository.findById(loggedUser.getId())
                .orElseThrow(() -> new RuntimeException("ucitel nenajdeny"));

        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase());

        consultationService.createRepeatingSlots(teacher, dayOfWeek, startTime, endTime, description, weeks);

        return "redirect:/teacher/create-page?success=repeating";
    }

    @PostMapping("/approve-reservation/{id}")
    public String approveReservation(@PathVariable Long id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        reservationService.approveReservation(id, loggedUser);
        return "redirect:/teacher/approvals?success=approved";
    }

    @PostMapping("/reject-reservation/{id}")
    public String rejectReservation(@PathVariable Long id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null || loggedUser.getRole() != Role.TEACHER) {
            return "redirect:/login";
        }

        reservationService.rejectReservation(id, loggedUser);
        return "redirect:/teacher/approvals?success=rejected";
    }
}
