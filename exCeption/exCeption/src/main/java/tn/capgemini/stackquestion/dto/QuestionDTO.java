package tn.capgemini.stackquestion.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionDTO {
    private int id;
    private String title;
    private String body;
    private Date createdDate;
    private List<String> tags;
    private int userId;
    private String name;
    private Integer voteCount = 0;
    private int voted;
    private String departement;
    private String projet;
}


