package tn.capgemini.stackquestion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.stackquestion.dto.KnowledgeDTO;
import tn.capgemini.stackquestion.services.knowledge.KnowledgeService;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "http://localhost:4200")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @PostMapping
    public ResponseEntity<?> postKnowledge(@RequestBody KnowledgeDTO dto) {
        KnowledgeDTO created = knowledgeService.addKnowledge(dto);
        if (created == null) {
            return ResponseEntity.badRequest().body("User not found or invalid data.");
        }
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<KnowledgeDTO>> getAllKnowledge() {
        return ResponseEntity.ok(knowledgeService.getAllKnowledge());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<KnowledgeDTO>> getUserKnowledge(@PathVariable Integer userId) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeByUserId(userId));
    }

    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody KnowledgeVoteRequest request) {
        knowledgeService.voteOnKnowledge(request.getKnowledgeId(), request.getUserId(), request.getVoteType());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{knowledgeId}/votes")
    public ResponseEntity<?> getQuestionVoteStats(@PathVariable int knowledgeId) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeVoteStats(knowledgeId));
    }

}
