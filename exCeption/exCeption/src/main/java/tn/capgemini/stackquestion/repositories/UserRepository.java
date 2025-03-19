package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findFirstByEmail(String email); // Changer User en Optional<User>
}
