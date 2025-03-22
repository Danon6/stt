package tn.capgemini.stackquestion.services.user;

import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
     UserDTO createUser(SignupDTO signupDTO);
     UserDTO addUser(UserDTO userDTO);
     List<UserDTO> getAllUsers(); // Récupérer tous les utilisateurs
     Optional<UserDTO> getUserById(Integer id); // Récupérer un utilisateur par ID
     UserDTO updateUser(Integer id, SignupDTO signupDTO); // Modifier un utilisateur
     boolean deleteUser(Integer id); // Supprimer un utilisateur
}
