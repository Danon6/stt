package tn.capgemini.stackquestion.services.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findFirstByEmail(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }

        User user = optionalUser.get();

        // ✅ Convert the role (e.g., "MEMBER") to "ROLE_MEMBER"
        String role = "ROLE_" + user.getTypeUser();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        // ✅ Return UserDetails with correct authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
