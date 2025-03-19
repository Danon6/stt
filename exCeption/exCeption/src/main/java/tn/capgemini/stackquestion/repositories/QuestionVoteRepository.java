package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.exCeption.entities.QuestionVote;

@Repository
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Long> {
}
