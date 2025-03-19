package tn.capgemini.stackquestion.services.vote;

import tn.capgemini.stackquestion.dto.QuestionVoteDto;

public interface VoteService {
    QuestionVoteDto addVoteToQuestion(QuestionVoteDto questionVoteDto);
}
