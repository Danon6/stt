package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findByAnswer(Answer answer);
}
