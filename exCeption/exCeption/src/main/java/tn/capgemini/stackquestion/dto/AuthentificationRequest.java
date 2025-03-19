package tn.capgemini.exCeption.dto;

import lombok.Data;

@Data
public class AuthentificationRequest {

    private String email;
    private String password;

}
