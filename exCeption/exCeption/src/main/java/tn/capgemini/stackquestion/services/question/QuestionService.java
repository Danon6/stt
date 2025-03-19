package tn.capgemini.stackquestion.services.question;

import tn.capgemini.stackquestion.dto.AllQuestionResponseDto;
import tn.capgemini.stackquestion.dto.QuestionDTO;
import tn.capgemini.stackquestion.dto.SingleQuestionDto;

public interface QuestionService {
    QuestionDTO addQuestion(QuestionDTO questionDto);

    AllQuestionResponseDto getAllQuestions(int pageNumber);

    SingleQuestionDto getQuestionById(int userId, int questionId);

    AllQuestionResponseDto getAllQuestionsByUserId(int userId, int pageNumber);
}
