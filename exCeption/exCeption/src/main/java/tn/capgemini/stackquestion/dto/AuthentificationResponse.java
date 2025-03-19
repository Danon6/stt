package tn.capgemini.stackquestion.dto;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.typeUser;

@Data
public class AuthentificationResponse {
    private String jwtToken;
    private typeUser typeUser;

    public AuthentificationResponse(String jwt, typeUser typeUser) {
        this.jwtToken = jwt;
        this.typeUser = typeUser;
    }
}
