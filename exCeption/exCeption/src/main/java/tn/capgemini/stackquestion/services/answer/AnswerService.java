package tn.capgemini.stackquestion.services.answer;

import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.dto.AnswerVoteDto;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.List;
import java.util.Map;

public interface AnswerService {
    AnswerDto postAnswer(AnswerDto answerDto, MultipartFile imageFile);

    List<AnswerDto> getAnswersByQuestionId(int questionId);

    AnswerVoteDto voteAnswer(int userId, int answerId, VoteType voteType);

    Map<String, Integer> getAnswerVoteStats(int answerId);
}
