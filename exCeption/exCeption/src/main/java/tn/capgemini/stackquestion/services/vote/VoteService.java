package tn.capgemini.exCeption.services.vote;

import tn.capgemini.exCeption.dto.QuestionVoteDto;

public interface VoteService {
    QuestionVoteDto addVoteToQuestion(QuestionVoteDto questionVoteDto);
}
