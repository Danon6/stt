package tn.capgemini.exCeption.services.question;

import tn.capgemini.exCeption.dto.AllQuestionResponseDto;
import tn.capgemini.exCeption.dto.QuestionDTO;
import tn.capgemini.exCeption.dto.SingleQuestionDto;

public interface QuestionService {
    QuestionDTO addQuestion(QuestionDTO questionDto);

    AllQuestionResponseDto getAllQuestions(int pageNumber);

    SingleQuestionDto getQuestionById(Long userId, Long questionId);

    AllQuestionResponseDto getAllQuestionsByUserId(Long userId, int pageNumber);
}
