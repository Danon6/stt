package tn.capgemini.stackquestion.services.question;

import tn.capgemini.stackquestion.dto.AllQuestionResponseDto;
import tn.capgemini.stackquestion.dto.QuestionDTO;
import tn.capgemini.stackquestion.dto.QuestionVoteDto;
import tn.capgemini.stackquestion.dto.SingleQuestionDto;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.List;
import java.util.Map;

public interface QuestionService {
    QuestionDTO addQuestion(QuestionDTO questionDto);

    AllQuestionResponseDto getAllQuestions(int pageNumber);
    AllQuestionResponseDto getAllQuestions(int pageNumber, int pageSize);
    SingleQuestionDto getQuestionById(int userId, int questionId);

    AllQuestionResponseDto getAllQuestionsByUserId(int userId, int pageNumber);

    QuestionVoteDto voteQuestion(int userId, int questionId, VoteType voteType);

    Map<String, Integer> getQuestionVoteStats(int questionId);
    QuestionDTO updateQuestion(int questionId, QuestionDTO updatedQuestionDto);
    void deleteQuestion(int questionId, int userId);
}
