package tn.capgemini.exCeption.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer post_id;
    private String title;
    private String content;
    private Number upvote;
    private Number downvote;
    private boolean status; //true : ya9bel comments , false : ysaker comments
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;
    @ManyToOne
   @JoinColumn(name = "user_id")
    private User autor;
    @OneToMany
    @JoinColumn(name = "tag_id")
    private List<Tags> tags;

}
