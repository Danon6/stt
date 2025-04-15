package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Image;
import tn.capgemini.stackquestion.entities.Question;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    // ✅ Existing method
    Image findByAnswer(Answer answer);

    Image findByAnswerId(int answerId);

    // ✅ New method for fetching image by question ID
    Image findByQuestionId(int questionId);
    Image findByQuestion_Id(Integer questionId);


}
