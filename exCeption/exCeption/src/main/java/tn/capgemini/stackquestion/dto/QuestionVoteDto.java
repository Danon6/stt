package tn.capgemini.stackquestion.dto;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.VoteType;

@Data
public class QuestionVoteDto {
    private int id;

    private VoteType voteType;

    private int userId;

    private int questionId;
}
