package tn.capgemini.stackquestion.repositories;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.entities.User;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAllByUser(User user, Pageable pageable); // ✅ this works
    Optional<Question> findByTitleIgnoreCase(String title);


}
