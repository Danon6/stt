package tn.capgemini.stackquestion.services.user;

import tn.capgemini.stackquestion.dto.SignupDTO;
import tn.capgemini.stackquestion.dto.UserDTO;

public interface UserService {
     UserDTO createUser(SignupDTO signupDTO);

}
