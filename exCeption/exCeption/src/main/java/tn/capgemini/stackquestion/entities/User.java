package tn.capgemini.stackquestion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.capgemini.stackquestion.dto.UserDTO;
import tn.capgemini.stackquestion.entities.enums.Status;
import tn.capgemini.stackquestion.entities.enums.typeUser;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer user_id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String phone;
    private Date dateNaissance;
    @Enumerated(EnumType.STRING)
    private typeUser typeUser;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Knowledge> knowledgeList;
    private String departement;
    private String projet;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public UserDTO mapUserToUserDTO() {
        return new UserDTO(this.user_id, this.name, this.email, this.phone, this.dateNaissance, this.typeUser,this.createdAt,this.updatedAt,this.departement, this.projet,this.status );
    }



}
