package tn.capgemini.stackquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.typeUser;

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
    private String password;
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
