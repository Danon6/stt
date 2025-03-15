package tn.capgemini.stackquestion.services.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.capgemini.stackquestion.dto.AllQuestionResponseDto;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.dto.QuestionDTO;
import tn.capgemini.stackquestion.dto.SingleQuestionDto;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.entities.QuestionVote;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.VoteType;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.repositories.ImageRepository;
import tn.capgemini.stackquestion.repositories.QuestionRepository;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    //Number of questions per page
    public static final int SEARCH_RESULT_PER_PAGE = 5;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Override
    public QuestionDTO addQuestion(QuestionDTO questionDto) {
        Optional<User> optionalUser = userRepository.findById(questionDto.getUserId());
        if (optionalUser.isPresent()){
            Question question = new Question();
            question.setTitle(questionDto.getTitle());
            question.setBody(questionDto.getBody());
            question.setCreatedDate(new Date());
            question.setTags(questionDto.getTags());
            question.setUser(optionalUser.get());
            Question createdQuestion =  questionRepository.save(question);

            QuestionDTO createdQuestionDto = new QuestionDTO();
            createdQuestionDto.setId(createdQuestion.getId());
            createdQuestionDto.setTitle(createdQuestion.getTitle());
            return createdQuestionDto;
        }
        return null;
    }

    @Override
    public AllQuestionResponseDto getAllQuestions(int pageNumber) {
// Specify the sorting direction and the field to sort by
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));

// Create a Pageable object with sorting
        Pageable paging = PageRequest.of(pageNumber, SEARCH_RESULT_PER_PAGE, sort);
        Page<Question> questionsPage =  questionRepository.findAll(paging);

        AllQuestionResponseDto allQuestionResponseDto = new AllQuestionResponseDto();

        allQuestionResponseDto.setQuestionDTOList(questionsPage.getContent().stream().map(Question::getQuestionDto).collect(Collectors.toList()));
        allQuestionResponseDto.setPageNumber(questionsPage.getPageable().getPageNumber());
        allQuestionResponseDto.setTotalPages(questionsPage.getTotalPages());
        return allQuestionResponseDto;
    }

    @Override
    public SingleQuestionDto getQuestionById(Long userId, Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if(optionalQuestion.isPresent()){
            //get the question and set it to singleQuestionDto
            SingleQuestionDto singleQuestionDto = new SingleQuestionDto();

            // vote check
            Question existingQuestion = optionalQuestion.get();
            Optional<QuestionVote> optionalQuestionVote = existingQuestion.getQuestionVoteList().stream().filter(
                    vote -> vote.getUser().getUser_id().equals(userId)
            ).findFirst();
            QuestionDTO questionDto = optionalQuestion.get().getQuestionDto();
            questionDto.setVoted(0);
            if(optionalQuestionVote.isPresent()){
                if(optionalQuestionVote.get().getVoteType().equals(VoteType.UPVOTE)){
                    questionDto.setVoted(1);
                }else{
                    questionDto.setVoted(-1);
                }
            }

            singleQuestionDto.setQuestionDTO(questionDto);

            //get the question's answers and set it to singleQuestionDto
            List<AnswerDto> answerDtoList = new ArrayList<>();
            List<Answer> answerList = answerRepository.findAllByQuestionId(questionId);
            for (Answer answer: answerList) {
                AnswerDto answerDto = answer.getAnswerDto();
                answerDto.setFile(imageRepository.findByAnswer(answer));
                answerDtoList.add(answerDto);
            }
            singleQuestionDto.setAnswerDtoList(answerDtoList);
            return singleQuestionDto;
        }
        return null;
    }

    @Override
    public AllQuestionResponseDto getAllQuestionsByUserId(Long userId, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, SEARCH_RESULT_PER_PAGE);
        Page<Question> questionsPage =  questionRepository.findAllByUser(userId, paging);

        AllQuestionResponseDto allQuestionResponseDto = new AllQuestionResponseDto();

        allQuestionResponseDto.setQuestionDTOList(questionsPage.getContent().stream().map(Question::getQuestionDto).collect(Collectors.toList()));
        allQuestionResponseDto.setPageNumber(questionsPage.getPageable().getPageNumber());
        allQuestionResponseDto.setTotalPages(questionsPage.getTotalPages());
        return allQuestionResponseDto;
    }
}
