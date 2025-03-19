package tn.capgemini.stackquestion.services.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("Username not found", null);
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());

    }
}
