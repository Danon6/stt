package tn.capgemini.exCeption.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer comment_id;
    private String comment;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;
    @ManyToOne
    private Post post;
    @ManyToOne
    private User user;
}
