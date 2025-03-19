package tn.capgemini.exCeption.dto;

import lombok.Data;
import tn.capgemini.exCeption.entities.enums.VoteType;

@Data
public class QuestionVoteDto {
    private Long id;

    private VoteType voteType;

    private Long userId;

    private Long questionId;
}
