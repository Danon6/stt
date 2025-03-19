package tn.capgemini.exCeption.services.user;

import tn.capgemini.exCeption.dto.SignupDTO;
import tn.capgemini.exCeption.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
     UserDTO createUser(SignupDTO signupDTO);
     List<UserDTO> getAllUsers(); // Récupérer tous les utilisateurs
     Optional<UserDTO> getUserById(Integer id); // Récupérer un utilisateur par ID
     UserDTO updateUser(Integer id, SignupDTO signupDTO); // Modifier un utilisateur
     void deleteUser(Integer id); // Supprimer un utilisateur
}
