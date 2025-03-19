package tn.capgemini.stackquestion.services.answer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.repositories.QuestionRepository;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AnswerServiceImpl implements AnswerService{
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public AnswerDto postAnswer(AnswerDto answerDto) {
        Optional<User> optionalUser = userRepository.findById(Math.toIntExact(answerDto.getUserId()));
        Optional<Question> optionalQuestion = questionRepository.findById(answerDto.getQuestionId());
        if (optionalUser.isPresent() && optionalQuestion.isPresent()){
            Answer answer = new Answer();
            answer.setBody(answerDto.getBody());
            answer.setCreatedDate(new Date());
            answer.setQuestion(optionalQuestion.get());
            answer.setUser(optionalUser.get());

            Answer createdAnswer = answerRepository.save(answer);

            AnswerDto createdAnswerDto = new AnswerDto();
            createdAnswerDto.setId(createdAnswer.getId());

            return  createdAnswerDto;
        }
        return null;
    }
}
