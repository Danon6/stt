package tn.capgemini.stackquestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.typeUser;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.Optional;

@SpringBootApplication
@Component
public class StackquestionApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(StackquestionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("this is working with Spring Boot");
		// Check if an admin user already exists
		Optional<User> existingAdmin = userRepository.findFirstByEmail("admin@capgemini.tn");

		if (existingAdmin.isEmpty()) {
			// Create a new admin user if none exists
			User user = new User();
			user.setName("Admin");
			user.setEmail("admin@capgemini.tn");
			user.setPassword(new BCryptPasswordEncoder().encode("GeminiAdmin")); // Encrypt password
			user.setTypeUser(typeUser.ADMIN);
			userRepository.save(user);
			System.out.println("✅ Admin user created: admin@capgemini.tn");
		} else {
			System.out.println("⚠️ Admin user already exists. Skipping creation.");
		}
	}
}
