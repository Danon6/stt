package tn.capgemini.stackquestion.dto;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.typeUser;

import java.util.Date;

@Data
public class SignupDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Date date;
    private typeUser typeUser;
    private String departement;
    private String projet;
}
