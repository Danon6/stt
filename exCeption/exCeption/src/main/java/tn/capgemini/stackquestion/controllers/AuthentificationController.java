package tn.capgemini.stackquestion.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.capgemini.stackquestion.Utils.JwtUtil;
import tn.capgemini.stackquestion.dto.AuthentificationRequest;
import tn.capgemini.stackquestion.dto.AuthentificationResponse;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.repositories.UserRepository;
import tn.capgemini.stackquestion.services.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "*")

@RestController
public class AuthentificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService UserDetailsService ;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public AuthentificationResponse createAuthentificationToken(@RequestBody AuthentificationRequest authentificationRequest, HttpServletResponse response) throws BadCredentialsException,DisabledException, UsernameNotFoundException, IOException, JSONException, ServletException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentificationRequest.getEmail(), authentificationRequest.getPassword()));
        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password.");

        }catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,"User is not activated.");
            return null;
        }
        final UserDetails userDetails= UserDetailsService.loadUserByUsername(authentificationRequest.getEmail());
        Optional<User> user =userRepository.findFirstByEmail(authentificationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(user.get().getEmail());
        // Return the authentication response with userId and username along with the jwtToken and typeUser
        return new AuthentificationResponse(
                jwt,
                user.get().getTypeUser(),  // User's role (MEMBER, ADMIN, etc.)
                user.get().getUser_id(),        // User's ID (assuming getId() returns the userId)
                user.get().getName()   // User's username
        );
    }
}
