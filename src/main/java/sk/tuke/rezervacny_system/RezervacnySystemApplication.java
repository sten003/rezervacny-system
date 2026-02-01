package sk.tuke.rezervacny_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@SpringBootApplication(scanBasePackages = "sk.tuke.rezervacny_system")
/*@ComponentScan(basePackages = {
        "sk.tuke.rezervacny_system",
        "sk.tuke.rezervacny_system.controller",
        "sk.tuke.rezervacny_system.service",
        "sk.tuke.rezervacny_system.repository",
        "sk.tuke.rezervacny_system.config"
})*/
public class RezervacnySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezervacnySystemApplication.class, args);
    }
}
