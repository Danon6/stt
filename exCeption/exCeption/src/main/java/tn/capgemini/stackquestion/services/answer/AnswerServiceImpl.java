package tn.capgemini.stackquestion.services.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.dto.AnswerVoteDto;
import tn.capgemini.stackquestion.entities.*;
import tn.capgemini.stackquestion.entities.enums.VoteType;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.repositories.ImageRepository;
import tn.capgemini.stackquestion.repositories.QuestionRepository;
import tn.capgemini.stackquestion.repositories.UserRepository;
import tn.capgemini.stackquestion.services.user.image.ImageService;

import java.io.IOException;
import java.util.*;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private tn.capgemini.stackquestion.repositories.AnswerVoteRepository answerVoteRepository;


    @Override
    public AnswerDto postAnswer(AnswerDto answerDto, MultipartFile imageFile) {
        Optional<User> optionalUser = userRepository.findById(Math.toIntExact(answerDto.getUserId()));
        Optional<Question> optionalQuestion = questionRepository.findById(answerDto.getQuestionId());

        if (optionalUser.isPresent() && optionalQuestion.isPresent()) {
            Answer answer = new Answer();
            answer.setBody(answerDto.getBody());
            answer.setCreatedDate(new Date());
            answer.setUser(optionalUser.get());
            answer.setQuestion(optionalQuestion.get());

            // ✅ Étape 1 : Enregistrer la réponse
            Answer createdAnswer = answerRepository.save(answer);

            // ✅ Étape 2 : Enregistrer l'image si elle existe
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    imageService.storeFile(imageFile, createdAnswer.getId());
                    answerDto.setImageUrl("/api/image/answer/" + createdAnswer.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                    // Optionnel : logger l'erreur ou renvoyer une erreur personnalisée
                }
            }

            // ✅ Étape 3 : Compléter le DTO de retour
            answerDto.setId(createdAnswer.getId());
            answerDto.setCreatedDate(createdAnswer.getCreatedDate());
            answerDto.setUsername(optionalUser.get().getName());

            return answerDto;
        }

        return null;
    }

    @Override
    public List<AnswerDto> getAnswersByQuestionId(int questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        List<AnswerDto> answerDtos = new ArrayList<>();

        for (Answer answer : answers) {
            AnswerDto dto = answer.getAnswerDto();

            Image image = imageRepository.findByAnswerId(answer.getId());
            if (image != null) {
                dto.setImageUrl("/api/image/answer/" + answer.getId());
            }

            answerDtos.add(dto);
        }

        return answerDtos;
    }
    public AnswerVoteDto voteAnswer(int userId, int answerId, VoteType voteType) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (optionalUser.isPresent() && optionalAnswer.isPresent()) {
            User user = optionalUser.get();
            Answer answer = optionalAnswer.get();

            // ✅ NOUVEAU : on passe directement les objets
            Optional<AnswerVote> existingVoteOpt = answerVoteRepository.findByUserAndAnswer(user, answer);

            AnswerVote vote;
            if (existingVoteOpt.isPresent()) {
                vote = existingVoteOpt.get();
                if (vote.getVoteType() == voteType) {
                    answerVoteRepository.delete(vote);
                    return null;
                } else {
                    vote.setVoteType(voteType);
                    vote = answerVoteRepository.save(vote);
                }
            } else {
                vote = new AnswerVote();
                vote.setUser(user);
                vote.setAnswer(answer);
                vote.setVoteType(voteType);
                vote = answerVoteRepository.save(vote);
            }

            AnswerVoteDto dto = new AnswerVoteDto();
            dto.setId(vote.getId());
            dto.setUserId(userId);
            dto.setAnswerId(answerId);
            dto.setVoteType(vote.getVoteType());

            return dto;
        }

        return null;
    }

    public Map<String, Integer> getAnswerVoteStats(int answerId) {
        int upvotes = answerVoteRepository.countByAnswerIdAndVoteType(answerId, VoteType.UPVOTE);
        int downvotes = answerVoteRepository.countByAnswerIdAndVoteType(answerId, VoteType.DOWNVOTE);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("upvotes", upvotes);
        stats.put("downvotes", downvotes);
        stats.put("score", upvotes - downvotes);

        return stats;
    }


}
