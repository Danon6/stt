package tn.capgemini.stackquestion.dto;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.VoteType;

@Data
public class AnswerVoteDto {
    private int id;
    private int userId;
    private int answerId;
    private VoteType voteType;
}
