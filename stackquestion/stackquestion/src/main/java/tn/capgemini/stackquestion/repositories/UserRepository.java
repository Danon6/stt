package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.User;

public interface UserRepository   extends JpaRepository<User, Long> {
    User findFirstByEmail(String email);
}
