package tn.capgemini.stackquestion.dto;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.typeUser;

@Data
public class AuthentificationResponse {
    private String jwtToken;
    private typeUser typeUser;
    private int user_id;
    private String name;

    public AuthentificationResponse(String jwt, typeUser typeUser,int user_id, String name) {
        this.jwtToken = jwt;
        this.typeUser = typeUser;
        this.user_id = user_id;
        this.name = name;
    }
}
