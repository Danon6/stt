package tn.capgemini.stackquestion.services.knowledge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.capgemini.stackquestion.dto.KnowledgeDTO;
import tn.capgemini.stackquestion.entities.Knowledge;
import tn.capgemini.stackquestion.entities.KnowledgeVote;
import tn.capgemini.stackquestion.entities.User;
import tn.capgemini.stackquestion.entities.enums.VoteType;
import tn.capgemini.stackquestion.repositories.KnowledgeRepository;
import tn.capgemini.stackquestion.repositories.KnowledgeVoteRepository;
import tn.capgemini.stackquestion.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Autowired
    private KnowledgeRepository knowledgeRepository;
    @Autowired
    private  KnowledgeVoteRepository kvpRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KnowledgeVoteRepository knowledgeVoteRepository;
    @Override
    public KnowledgeDTO addKnowledge(KnowledgeDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        if (userOpt.isEmpty()) return null;

        Knowledge knowledge = new Knowledge();
        knowledge.setTitle(dto.getTitle());
        knowledge.setDescription(dto.getDescription());
        knowledge.setContent(dto.getContent());
        knowledge.setCreatedDate(new Date());
        knowledge.setUser(userOpt.get());
        knowledge.setDepartement(dto.getDepartement());
        knowledge.setProjet(dto.getProjet());
        if (dto.getImageBase64() != null && !dto.getImageBase64().isEmpty() &&
                dto.getImageType() != null && dto.getImageBase64().contains(",")) {

            String[] imageParts = dto.getImageBase64().split(",", 2);
            if (imageParts.length == 2) {
                knowledge.setImage(Base64.getDecoder().decode(imageParts[1]));
                knowledge.setImageType(dto.getImageType());
            }
        }

        if (dto.getFileBase64() != null && !dto.getFileBase64().isEmpty() &&
                dto.getFileType() != null && dto.getFileBase64().contains(",")) {

            String[] fileParts = dto.getFileBase64().split(",", 2);
            if (fileParts.length == 2) {
                knowledge.setFile(Base64.getDecoder().decode(fileParts[1]));
                knowledge.setFileType(dto.getFileType());
            }
        }




        Knowledge saved = knowledgeRepository.save(knowledge);
        return saved.toDTO();
    }

    @Override
    public List<KnowledgeDTO> getAllKnowledge() {
        return knowledgeRepository.findAll()
                .stream()
                .map(Knowledge::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeDTO> getKnowledgeByUserId(Integer userId) {
        return knowledgeRepository.findAllByUserId(userId, null)
                .stream()
                .map(Knowledge::toDTO)
                .collect(Collectors.toList());
    }

    public void voteOnKnowledge(Integer knowledgeId, Integer userId, VoteType voteType) {
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
                .orElseThrow(() -> new RuntimeException("Knowledge not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<KnowledgeVote> existingVoteOpt = knowledgeVoteRepository.findByKnowledgeAndUser(knowledge, user);

        if (existingVoteOpt.isPresent()) {
            KnowledgeVote existingVote = existingVoteOpt.get();
            if (existingVote.getVoteType() == voteType) {
                knowledgeVoteRepository.delete(existingVote); // toggle
            } else {
                existingVote.setVoteType(voteType); // switch
                knowledgeVoteRepository.save(existingVote);
            }
        } else {
            KnowledgeVote vote = new KnowledgeVote();
            vote.setKnowledge(knowledge);
            vote.setUser(user);
            vote.setVoteType(voteType);
            knowledgeVoteRepository.save(vote);
        }
    }

    @Override
    public Map<String, Integer> getKnowledgeVoteStats(int knowledgeId) {
        int upvotes = kvpRepository.countByKnowledgeIdAndVoteType(knowledgeId, VoteType.UPVOTE);
        int downvotes = kvpRepository.countByKnowledgeIdAndVoteType(knowledgeId, VoteType.DOWNVOTE);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("upvotes", upvotes);
        stats.put("downvotes", downvotes);
        stats.put("score", upvotes - downvotes);

        return stats;
    }
    @Override
    public KnowledgeDTO updateKnowledge(Integer id, KnowledgeDTO updatedDTO) {
        Knowledge existing = knowledgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Knowledge not found"));

        if (!existing.getUser().getUser_id().equals(updatedDTO.getUserId())) {
            throw new RuntimeException("Unauthorized: You can only update your own knowledge.");
        }

        existing.setTitle(updatedDTO.getTitle());
        existing.setDescription(updatedDTO.getDescription());
        existing.setContent(updatedDTO.getContent());
        existing.setDepartement(updatedDTO.getDepartement());
        existing.setProjet(updatedDTO.getProjet());

        if (updatedDTO.getImageBase64() != null && updatedDTO.getImageBase64().contains(",")) {
            String[] imgParts = updatedDTO.getImageBase64().split(",", 2);
            existing.setImage(Base64.getDecoder().decode(imgParts[1]));
            existing.setImageType(updatedDTO.getImageType());
        }

        if (updatedDTO.getFileBase64() != null && updatedDTO.getFileBase64().contains(",")) {
            String[] fileParts = updatedDTO.getFileBase64().split(",", 2);
            existing.setFile(Base64.getDecoder().decode(fileParts[1]));
            existing.setFileType(updatedDTO.getFileType());
        }

        Knowledge saved = knowledgeRepository.save(existing);
        return saved.toDTO();
    }

    @Override
    public void deleteKnowledge(Integer id, Integer userId) {
        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Knowledge not found"));

        if (!knowledge.getUser().getUser_id().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own knowledge.");
        }

        knowledgeRepository.delete(knowledge);
    }

}
