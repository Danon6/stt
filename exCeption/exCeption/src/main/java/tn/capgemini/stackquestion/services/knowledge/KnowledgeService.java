package tn.capgemini.stackquestion.services.knowledge;

import tn.capgemini.stackquestion.dto.KnowledgeDTO;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.List;
import java.util.Map;

public interface KnowledgeService {
    KnowledgeDTO addKnowledge(KnowledgeDTO knowledgeDTO);
    List<KnowledgeDTO> getAllKnowledge();
    List<KnowledgeDTO> getKnowledgeByUserId(Integer userId);
    void voteOnKnowledge(Integer knowledgeId, Integer userId, VoteType voteType);
    Map<String, Integer> getKnowledgeVoteStats(int knowledgeId);
    KnowledgeDTO updateKnowledge(Integer id, KnowledgeDTO updatedDTO);
    void deleteKnowledge(Integer id, Integer userId);

}
