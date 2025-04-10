package tn.capgemini.stackquestion.dto;

import lombok.Data;

import java.util.Date;

@Data
public class KnowledgeDTO {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private String imageBase64;
    private String imageType;
    private String fileBase64;
    private String fileType;
    private String departement;
    private String projet;
    private Integer userId;
    private String userName;
    private Date createdDate;
    private int voteCount;

}
