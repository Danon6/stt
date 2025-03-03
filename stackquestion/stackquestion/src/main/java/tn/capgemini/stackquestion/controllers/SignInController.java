package tn.capgemini.stackquestion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.capgemini.stackquestion.dto.SigninDTO;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.services.user.UserService;
import tn.capgemini.stackquestion.services.user.UserServiceImpl;

@RestController
public class SignInController {

    @Autowired
    private UserServiceImpl userService;
    @PostMapping("/signin")
    public ResponseEntity<?> signingUser(@RequestBody SigninDTO signinDTO) {
        try {
            UserDTO createUser = userService.loginUser(signinDTO);
            return ResponseEntity.ok().body(createUser);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
}
}
