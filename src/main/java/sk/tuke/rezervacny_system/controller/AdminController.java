package sk.tuke.rezervacny_system.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.rezervacny_system.entity.Role;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.UserRepository;
import sk.tuke.rezervacny_system.service.DatabaseCleanupService;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DatabaseCleanupService databaseCleanupService;
    private final UserRepository userRepository;

    public AdminController(DatabaseCleanupService databaseCleanupService, UserRepository userRepository) {
        this.databaseCleanupService = databaseCleanupService;
        this.userRepository = userRepository;
    }

    @GetMapping("/overview")
    public String overview(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("adminName", loggedUser.getFullName());
        model.addAttribute("users", userRepository.findAll());

        return "admin/overview";
    }

    @PostMapping("/cleanup-database")
    public String cleanupDatabase(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");

        //kontrola pre istotu aby to mohol spustit len admin
        if (loggedUser != null && loggedUser.getRole() == Role.ADMIN) {
            databaseCleanupService.deleteAllSlotsAndReservations();
        }

        return "redirect:/admin/overview";
    }

    @GetMapping("/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        User userToEdit = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Používateľ nenájdený"));

        model.addAttribute("userToEdit", userToEdit);
        //informacia ci edituje sam seba aby sa dala zablokovat zmena role
        model.addAttribute("isSelf", loggedUser.getId().equals(userToEdit.getId()));

        return "admin/edit-user";
    }

    //ulozenie zmien
    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String username,
                             @RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam Role role,
                             HttpSession session) {

        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Používateľ nenájdený"));

        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        //ak edituje seba nemoze zmenit rolu
        if (loggedUser.getId().equals(user.getId())) {
            user.setRole(Role.ADMIN);
        }
        else {
            user.setRole(role);
        }

        userRepository.save(user);

        return "redirect:/admin/overview";
    }

    @GetMapping("/create-user")
    public String createUserForm(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        return "admin/create-user";
    }

    //ulozenie noveho pouzivatela
    @PostMapping("/create-user")
    public String createUser(@RequestParam String username,
                             @RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam Role role,
                             HttpSession session) {

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        //kontrola ci uz neexstuje
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/admin/create-user?error";
        }

        User newUser = new User(null, username, fullName, email, password, role, true);
        userRepository.save(newUser);

        return "redirect:/admin/overview";
    }

    //vymazanie
    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        //admin nemoze zmazat svoj ucet
        if (loggedUser.getId().equals(id)) {
            return "redirect:/admin/overview";
        }

        userRepository.deleteById(id);
        return "redirect:/admin/overview";
    }
}