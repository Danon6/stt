package tn.capgemini.exCeption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.capgemini.exCeption.entities.enums.typeUser;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private Date date;
    private typeUser typeUser;
    private Integer departementId;  // On stocke l'ID au lieu de l'objet entier+
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    public  UserDTO(Integer userId,String name, String email, String phone,Date dateNasissance, typeUser typeUser,Date createdAt,Date updatedAt) {
        this.userId=userId;
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.date=dateNasissance;
        this.typeUser=typeUser;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;

    }
}
