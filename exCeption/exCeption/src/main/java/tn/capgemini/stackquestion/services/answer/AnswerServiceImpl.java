package tn.capgemini.stackquestion.services.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Image;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.repositories.ImageRepository;
import tn.capgemini.stackquestion.repositories.QuestionRepository;
import tn.capgemini.stackquestion.repositories.UserRepository;
import tn.capgemini.stackquestion.services.user.image.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
}
