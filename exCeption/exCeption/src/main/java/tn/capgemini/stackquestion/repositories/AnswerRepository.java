package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Answer;

import java.util.List;

@Repository
public interface AnswerRepository  extends JpaRepository<Answer, Integer> {

    List<Answer> findByQuestionId(int questionId);

}
