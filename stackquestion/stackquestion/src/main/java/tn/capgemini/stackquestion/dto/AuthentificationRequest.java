package tn.capgemini.stackquestion.dto;

import lombok.Data;

@Data
public class AuthentificationRequest {

    private String email;
    private String password;

}
