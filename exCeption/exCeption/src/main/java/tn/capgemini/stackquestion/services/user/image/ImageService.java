package tn.capgemini.stackquestion.services.user.image;

import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.entities.Question;

import java.io.IOException;

public interface ImageService {
    void storeFile(MultipartFile multipartFile, int answerId) throws IOException;
    void storeFileForQuestion(MultipartFile multipartFile, int questionId) throws IOException;
    void updateQuestionImage(MultipartFile file, int questionId) throws IOException;
}
