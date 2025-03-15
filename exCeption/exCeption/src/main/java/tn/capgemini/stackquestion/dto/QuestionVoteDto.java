package tn.capgemini.stackquestion.dto;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.VoteType;

@Data
public class QuestionVoteDto {
    private Long id;

    private VoteType voteType;

    private Long userId;

    private Long questionId;
}
