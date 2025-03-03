package tn.capgemini.stackquestion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.services.user.UserService;
import tn.capgemini.stackquestion.services.user.UserServiceImpl;

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
