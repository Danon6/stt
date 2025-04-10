package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.entities.QuestionVote;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.Optional;

@Repository
public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Integer> {

    // Trouver un vote existant par user et question
    Optional<QuestionVote> findByUserAndQuestion(User user, Question question);

    // Compter le nombre de votes (upvotes ou downvotes) pour une question
    int countByQuestionIdAndVoteType(Integer questionId, VoteType voteType);
}
