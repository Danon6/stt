package tn.capgemini.exCeption.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.exCeption.dto.SignupDTO;
import tn.capgemini.exCeption.dto.UserDTO;
import tn.capgemini.exCeption.services.user.UserServiceImpl;
@CrossOrigin(origins = "*")

@RestController
public class SignupController {
    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
        try {
            UserDTO createUser = userService.createUser(signupDTO);
            return ResponseEntity.ok().body(createUser);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
