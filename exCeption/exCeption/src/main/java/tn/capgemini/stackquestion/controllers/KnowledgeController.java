package tn.capgemini.stackquestion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.stackquestion.dto.KnowledgeDTO;
import tn.capgemini.stackquestion.services.knowledge.KnowledgeService;
import tn.capgemini.stackquestion.services.user.UserService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "http://localhost:4200")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<?> postKnowledge(@RequestBody KnowledgeDTO dto) {
        if (!userService.isUserActive(dto.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not active.");
        }
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
        if (!userService.isUserActive(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not active.");
        }
        knowledgeService.voteOnKnowledge(request.getKnowledgeId(), request.getUserId(), request.getVoteType());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{knowledgeId}/votes")
    public ResponseEntity<?> getQuestionVoteStats(@PathVariable int knowledgeId) {
        return ResponseEntity.ok(knowledgeService.getKnowledgeVoteStats(knowledgeId));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateKnowledge(@PathVariable Integer id, @RequestBody KnowledgeDTO dto) {
        if (!userService.isUserActive(dto.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not active.");
        }
        try {
            KnowledgeDTO updated = knowledgeService.updateKnowledge(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteKnowledge(@PathVariable Integer id, @RequestParam Integer userId) {
        if (!userService.isUserActive(userId    )) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not active.");
        }
        try {
            knowledgeService.deleteKnowledge(id, userId);
            return ResponseEntity.ok(Collections.singletonMap("message", "✅ Knowledge deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }
}
