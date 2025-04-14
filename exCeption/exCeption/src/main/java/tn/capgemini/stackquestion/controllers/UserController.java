package tn.capgemini.stackquestion.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.Status;
import tn.capgemini.stackquestion.services.user.UserServiceImpl;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    // ✅ Récupérer tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }

    // ✅ Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) { // 🔥 Correction : @PathVariable
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }

    // ✅ Ajouter un utilisateur
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDTO signupDTO) {
        try {
            return ResponseEntity.ok(userService.addUser(signupDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }

    // ✅ Modifier un utilisateur
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody SignupDTO signupDTO) { // 🔥 Correction : @PathVariable
        try {
            return ResponseEntity.ok(userService.updateUser(id, signupDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean isDeleted = userService.deleteUser(id);
            if (isDeleted) {
                response.put("message", "Utilisateur supprimé avec succès !");
                return ResponseEntity.ok(response); // ✅ Retourne une réponse JSON valide
            } else {
                response.put("error", "Utilisateur introuvable !");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException e) {
            response.put("error", "Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            response.put("error", "Erreur serveur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable int id) {
        try {
            User user = userService.getUserEntityById(id); // ⚠️ Make sure getUserEntityById exists in your service and returns a `User` object

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé !");
            }

            user.setStatus(Status.INACTIVE);
            userService.save(user); // Or userRepository.save(user); depending on how your service is structured

            return ResponseEntity.ok("✅ Utilisateur banni avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }

    @PutMapping("/unban/{id}")
    public ResponseEntity<?> unbanUser(@PathVariable int id) {
        try {
            User user = userService.getUserEntityById(id);
            user.setStatus(Status.ACTIVE);
            userService.save(user);
            return ResponseEntity.ok("✅ Utilisateur réactivé !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur : " + e.getMessage());
        }
    }



}
