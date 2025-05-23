package tn.capgemini.stackquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.capgemini.stackquestion.entities.Image;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private int id;

    private String body;

    private Date createdDate;

    private  int questionId;

    private Long userId;

    private String username;

    private String imageUrl; // pour le frontend (base64 ou URL)
}
