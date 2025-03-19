package tn.capgemini.stackquestion.services.user.image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Image;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.repositories.ImageRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public void storeFile(MultipartFile multipartFile, int answerId) throws IOException {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()){
            String fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Image image = new Image();
            image.setName(fileName);
            image.setAnswer(optionalAnswer.get());
            image.setType(multipartFile.getContentType());
            image.setData(multipartFile.getBytes());
            imageRepository.save(image);
        }else{
            throw new IOException("Answer not found");
        }
    }
}

