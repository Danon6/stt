package tn.capgemini.stackquestion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String type;

    @Lob
    @Column(name = "data", length = 10000)
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = true) // ⬅️ Les deux doivent être nullable
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Question question;

}
