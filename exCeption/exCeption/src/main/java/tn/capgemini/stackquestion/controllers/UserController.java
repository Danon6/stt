package tn.capgemini.stackquestion.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.services.user.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserServiceImpl userService ;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById( @RequestParam int id){
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDTO signupDTO){
        try {
            return ResponseEntity.ok(userService.addUser(signupDTO));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody SignupDTO signupDTO , @RequestParam int id ){
        try {
            return ResponseEntity.ok(userService.updateUser(id,signupDTO));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@RequestParam int id ){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
