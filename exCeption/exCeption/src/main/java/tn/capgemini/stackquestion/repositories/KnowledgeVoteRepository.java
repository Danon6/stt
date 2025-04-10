package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.stackquestion.entities.Knowledge;
import tn.capgemini.stackquestion.entities.KnowledgeVote;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.Optional;

@Repository
public interface KnowledgeVoteRepository extends JpaRepository<KnowledgeVote, Long> {
    Optional<KnowledgeVote> findByKnowledgeAndUser(Knowledge knowledge, User user);
    int countByKnowledgeIdAndVoteType(Integer knowledgeId, VoteType voteType);
}
