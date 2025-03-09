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

    private Long id;

    private String body;

    private Date createdDate;

    private  Long questionId;

    private Long userId;

    private String username;

    private Image file;
}
