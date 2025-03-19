package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.QuestionVote;

@Repository
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Integer> {
}
