package tn.capgemini.exCeption.services.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.capgemini.exCeption.dto.QuestionVoteDto;
import tn.capgemini.exCeption.entities.Question;
import tn.capgemini.exCeption.entities.QuestionVote;
import tn.capgemini.exCeption.entities.User;
import tn.capgemini.exCeption.entities.enums.VoteType;
import tn.capgemini.exCeption.repositories.QuestionRepository;
import tn.capgemini.exCeption.repositories.QuestionVoteRepository;
import tn.capgemini.exCeption.repositories.UserRepository;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService{

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionVoteRepository questionVoteRepository;


    @Override
    public QuestionVoteDto addVoteToQuestion(QuestionVoteDto questionVoteDto) {
        Optional<User> optionalUser = userRepository.findById(questionVoteDto.getUserId());
        Optional<Question> optionalQuestion = questionRepository.findById(questionVoteDto.getQuestionId());

        if (optionalQuestion.isPresent() && optionalUser.isPresent()){
            QuestionVote questionVote = new QuestionVote();

            Question existingQuestion = optionalQuestion.get();

            questionVote.setVoteType(questionVoteDto.getVoteType());

            if(questionVote.getVoteType() == VoteType.UPVOTE){
                existingQuestion.setVoteCount(existingQuestion.getVoteCount() + 1);
            }else {
                existingQuestion.setVoteCount(existingQuestion.getVoteCount() - 1);
            }

            questionVote.setQuestion(optionalQuestion.get());
            questionVote.setUser(optionalUser.get());

            questionRepository.save(existingQuestion);

            QuestionVote votedQuestion = questionVoteRepository.save(questionVote);
            QuestionVoteDto questionVotedDto = new QuestionVoteDto();
            questionVotedDto.setId(votedQuestion.getId());
            return questionVotedDto;
        }
        return null;
    }
}
