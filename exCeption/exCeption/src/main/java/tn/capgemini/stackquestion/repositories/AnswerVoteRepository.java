package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Answer;
import tn.capgemini.stackquestion.entities.AnswerVote;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.Optional;

@Repository
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Integer> {

    // Vérifier si un utilisateur a déjà voté pour une réponse
    Optional<AnswerVote> findByUserAndAnswer(User user, Answer answer);

    // Compter les votes UP ou DOWN pour une réponse
    int countByAnswerIdAndVoteType(Integer answerId, VoteType voteType);
}
