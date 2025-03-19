package tn.capgemini.exCeption.dto;

import lombok.Data;
import tn.capgemini.exCeption.entities.enums.typeUser;

@Data
public class AuthentificationResponse {
    private String jwtToken;
    private typeUser typeUser;

    public AuthentificationResponse(String jwt, typeUser typeUser) {
        this.jwtToken = jwt;
        this.typeUser = typeUser;
    }
}
