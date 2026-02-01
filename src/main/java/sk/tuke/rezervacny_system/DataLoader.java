package sk.tuke.rezervacny_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.tuke.rezervacny_system.entity.Role;
import sk.tuke.rezervacny_system.entity.User;
import sk.tuke.rezervacny_system.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.findByUsername("teacher").isPresent()) {
            User teacher = new User(null, "teacher", "Ing. Miroslav Pompa, PhD.", "miroslav.pompa@tuke.sk", "password", Role.TEACHER, true);
            userRepository.save(teacher);
        }

        if (!userRepository.findByUsername("student").isPresent()) {
            User student = new User(null, "student", "Palo Ščerba", "palo.scerba@student.tuke.sk", "password", Role.STUDENT, true);
            userRepository.save(student);
        }
    }
}
