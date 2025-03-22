package tn.capgemini.stackquestion.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.typeUser;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Récupère l'email de l'utilisateur connecté
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    @Transactional
    // this is for register user
    public UserDTO createUser(SignupDTO signupDTO) {
        // Vérifier si l'email se termine bien par "@capgemini.tn"
        if (!signupDTO.getEmail().toLowerCase().endsWith("@capgemini.tn")) {
            throw new RuntimeException("Adresse email invalide : seuls les emails @capgemini.tn sont autorisés !");
        }

        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        user.setPhone(signupDTO.getPhone());
        user.setDateNaissance(signupDTO.getDate());
        user.setTypeUser(typeUser.MEMBER); // Par défaut, on assigne MEMBER
        userRepository.save(user);

        return user.mapUserToUserDTO();
    }

    @Override
    @Transactional
    //this is for the admin to add user manually
    public UserDTO addUser(UserDTO userDTO) {
        // Vérifier si l'email se termine bien par "@capgemini.tn"
        if (!userDTO.getEmail().toLowerCase().endsWith("@capgemini.tn")) {
            throw new RuntimeException("Adresse email invalide : seuls les emails @capgemini.tn sont autorisés !");
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user.setDateNaissance(userDTO.getDate());
        user.setTypeUser(userDTO.getTypeUser());
        User res =  userRepository.save(user);
        return res.mapUserToUserDTO();
    }
    @Override
    public List<UserDTO> getAllUsers() {
        User adminUser = getAuthenticatedUser();
        if (adminUser.getTypeUser() != typeUser.ADMIN) {
            throw new RuntimeException("Accès refusé : Seul un ADMIN peut voir la liste des utilisateurs !");
        }
        return userRepository.findAll().stream()
                .map(User::mapUserToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(Integer id) {
        User adminUser = getAuthenticatedUser();
        if (adminUser.getTypeUser() != typeUser.ADMIN) {
            throw new RuntimeException("Accès refusé : Seul un ADMIN peut voir les détails d'un utilisateur !");
        }
        return userRepository.findById(id).map(User::mapUserToUserDTO);
    }

    @Transactional
    @Override
    public UserDTO updateUser(Integer id, SignupDTO signupDTO) {
        User adminUser = getAuthenticatedUser();
        if (adminUser.getTypeUser() != typeUser.ADMIN) {
            throw new RuntimeException("Accès refusé : Seul un ADMIN peut modifier un utilisateur !");
        }

        return userRepository.findById(id).map(user -> {
            user.setName(signupDTO.getName());
            user.setEmail(signupDTO.getEmail());
            user.setPhone(signupDTO.getPhone());
            user.setDateNaissance(signupDTO.getDate());
            if (signupDTO.getPassword() != null && !signupDTO.getPassword().isEmpty()) {
                user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
            }
            userRepository.save(user);
            return user.mapUserToUserDTO();
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Transactional
    @Override
    public boolean deleteUser(Integer id) {
        User adminUser = getAuthenticatedUser();
        if (adminUser.getTypeUser() != typeUser.ADMIN) {
            throw new RuntimeException("Accès refusé : Seul un ADMIN peut supprimer un utilisateur !");
        }

        if (userRepository.existsById(id)) { // ✅ Vérifie si l'utilisateur existe
            userRepository.deleteById(id);
            return true; // ✅ Retourne TRUE si la suppression est réussie
        } else {
            return false; // ❌ Retourne FALSE si l'utilisateur n'existe pas
        }
    }

}
