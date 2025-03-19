package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.exCeption.entities.Answer;

import java.util.List;

@Repository
public interface AnswerRepository  extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);
}
