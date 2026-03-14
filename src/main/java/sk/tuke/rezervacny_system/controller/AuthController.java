package sk.tuke.rezervacny_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.tuke.rezervacny_system.entity.Role;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.UserRepository;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    //--------------------------------register--------------------------------
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String fullName,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  Model model) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        Optional<User> existingEmail = userRepository.findByEmail(email);

        if (existingUser.isPresent() || existingEmail.isPresent()) {
            model.addAttribute("error", "Používateľské meno, alebo email je už obsadené.");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password); //pridat hashovanie
        user.setRole(Role.STUDENT); //defaultna rola student
        user.setEnabled(true);

        userRepository.save(user);

        return "redirect:/login?registered";
    }

    //--------------------------------login--------------------------------
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String registered, Model model) {
        if (registered != null) {
            model.addAttribute("success", "Registrácia úspešná! Teraz sa môžete prihlásiť.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Nesprávny email alebo heslo.");
            return "login";
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Nesprávny email alebo heslo.");
            return "login";
        }

        if (!user.isEnabled()) {
            model.addAttribute("error", "Váš účet je deaktivovaný.");
            return "login";
        }

        session.setAttribute("user", user);

        if (user.getRole() == Role.TEACHER) {
            return "redirect:/teacher/overview";
        }
        else if (user.getRole() == Role.STUDENT) {
            return "redirect:/student/overview";
        }
        else if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/overview";
        }

        return "redirect:/login";
    }

    //--------------------------------logout--------------------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}