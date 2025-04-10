package tn.capgemini.stackquestion.controllers;

import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.VoteType;

@Data
public class KnowledgeVoteRequest {
    private Integer knowledgeId;
    private Integer userId;
    private VoteType voteType;
}

