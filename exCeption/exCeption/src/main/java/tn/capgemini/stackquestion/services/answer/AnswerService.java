package tn.capgemini.stackquestion.services.answer;

import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.dto.AnswerDto;

import java.util.List;

public interface AnswerService{
    AnswerDto postAnswer(AnswerDto answerDto, MultipartFile imageFile);

    List<AnswerDto> getAnswersByQuestionId(int questionId);

}
