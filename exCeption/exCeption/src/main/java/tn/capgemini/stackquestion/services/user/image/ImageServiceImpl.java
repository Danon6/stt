package tn.capgemini.stackquestion.services.user.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Image;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.repositories.ImageRepository;
import tn.capgemini.stackquestion.repositories.QuestionRepository;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void storeFile(MultipartFile multipartFile, int answerId) throws IOException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IOException("❌ Answer not found with ID: " + answerId));

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Image image = new Image();
        image.setName(fileName);
        image.setType(multipartFile.getContentType());
        image.setData(multipartFile.getBytes());
        image.setAnswer(answer);

        answer.setImage(image); // ✅ Relation bidirectionnelle

        imageRepository.save(image);
    }


    @Override
    public void storeFileForQuestion(MultipartFile multipartFile, int questionId) throws IOException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IOException("❌ Question not found with ID: " + questionId));

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Image image = new Image();
        image.setName(fileName);
        image.setType(multipartFile.getContentType());
        image.setData(multipartFile.getBytes());
        image.setQuestion(question);

        imageRepository.save(image);
    }
    @Override
    public void updateQuestionImage(MultipartFile file, int questionId) throws IOException {
        // Delete existing if needed
        Image existing = imageRepository.findByQuestionId(questionId);
        if (existing != null) {
            imageRepository.delete(existing);
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Question not found with ID: " + questionId));

        Image newImage = new Image();
        newImage.setName(file.getOriginalFilename());
        newImage.setType(file.getContentType());
        newImage.setData(file.getBytes());
        newImage.setQuestion(question);  // ✅ Set the full object here

        imageRepository.save(newImage);

    }

}
