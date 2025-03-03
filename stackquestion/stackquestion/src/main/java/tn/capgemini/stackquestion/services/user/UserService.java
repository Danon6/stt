package tn.capgemini.stackquestion.services.user;

import org.springframework.transaction.annotation.Transactional;
import tn.capgemini.stackquestion.dto.SigninDTO;
import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.entities.User;

public interface UserService {
     UserDTO createUser(SignupDTO signupDTO);
     UserDTO loginUser(SigninDTO signinDTO);

}
