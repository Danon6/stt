package tn.capgemini.exCeption.dto;

import lombok.Data;
import tn.capgemini.exCeption.entities.enums.typeUser;

import java.util.Date;

@Data
public class SignupDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Date date;
    private typeUser typeUser;
}
