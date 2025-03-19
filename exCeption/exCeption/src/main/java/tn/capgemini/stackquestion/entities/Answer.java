package tn.capgemini.stackquestion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tn.capgemini.stackquestion.dto.AnswerDto;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "body", length = 512)
    private String body;

    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Question question;

    public AnswerDto getAnswerDto() {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(id);
        answerDto.setBody(body);
        answerDto.setUserId(Long.valueOf(user.getUser_id()));
        answerDto.setQuestionId(question.getId());
        answerDto.setUsername(user.getName());
        answerDto.setCreatedDate(createdDate);
        return answerDto;
    }
}
