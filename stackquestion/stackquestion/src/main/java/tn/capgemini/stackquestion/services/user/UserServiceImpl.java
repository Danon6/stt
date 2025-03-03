package tn.capgemini.stackquestion.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.capgemini.stackquestion.dto.SigninDTO;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.typeUser;
import tn.capgemini.stackquestion.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDTO createUser(SignupDTO signupDTO) {
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        user.setPhone(signupDTO.getPhone());
        user.setDateNaissance(signupDTO.getDate());
        user.setTypeUser(typeUser.MEMBER);
        userRepository.save(user); // Sauvegarde et récupération de l'utilisateur en BDD
        return user.mapUserToUserDTO();
    }

    @Override
    public UserDTO loginUser(SigninDTO signinDTO) {
        User user = userRepository.findFirstByEmail(signinDTO.getEmail());
        if (user == null) {
            return null;
        }else if (new BCryptPasswordEncoder().matches(signinDTO.getPassword(), user.getPassword())) {
            return user.mapUserToUserDTO();
        }else {
            return null;
        }
    }
}
