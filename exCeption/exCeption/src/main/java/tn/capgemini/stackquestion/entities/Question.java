package tn.capgemini.stackquestion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tn.capgemini.stackquestion.dto.QuestionDTO;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Lob
    @Column(name = "body", length = 512)
    private String body;

    private Date createdDate;

    @ElementCollection(targetClass = String.class)
    private List<String> tags; // remains the same (stored in questions_tags table)

    private String tagsString; // new field to store tags as comma-separated in questions table

    private Integer voteCount = 0;
    private String departement;
    private String projet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionVote> questionVoteList;

    public QuestionDTO getQuestionDto() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(id);
        questionDTO.setTitle(title);
        questionDTO.setBody(body);
        questionDTO.setCreatedDate(createdDate);
        questionDTO.setUserId(user.getUser_id());
        questionDTO.setTags(tags); // returning list
        questionDTO.setVoteCount(voteCount);
        questionDTO.setName(user.getName());
        questionDTO.setDepartement(departement);
        questionDTO.setProjet(projet);
        return questionDTO;
    }
}

