package tn.capgemini.stackquestion.services.user.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void storeFile(MultipartFile multipartFile, int answerId) throws IOException;

    // ✅ New method for question image upload
    void storeFileForQuestion(MultipartFile multipartFile, int questionId) throws IOException;
}
