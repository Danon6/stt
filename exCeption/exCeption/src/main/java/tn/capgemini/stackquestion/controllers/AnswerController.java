package tn.capgemini.stackquestion.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.dto.AnswerVoteDto;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.services.answer.AnswerServiceImpl;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private AnswerRepository answerRepository;

    // ✅ Add new answer
    @PostMapping
    public ResponseEntity<?> addAnswer(
            @RequestParam("answer") String answerJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            // ✅ Convertir JSON stringifié en DTO (depuis FormData)
            ObjectMapper objectMapper = new ObjectMapper();
            AnswerDto answerDto = objectMapper.readValue(answerJson, AnswerDto.class);

            AnswerDto createdAnswerDto = answerService.postAnswer(answerDto, imageFile);
            if (createdAnswerDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswerDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Invalid JSON or image: " + e.getMessage());
        }
    }




    // ✅ Get all answers
    @GetMapping("/all")
    public ResponseEntity<?> getAllAnswers() {
        return ResponseEntity.ok(answerRepository.findAll());
    }

    // ✅ Get all answers by question ID
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerDto>> getAnswersByQuestionId(@PathVariable int questionId) {
        List<AnswerDto> answers = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }
    // ✅ Voter pour une réponse
    @PostMapping("/vote")
    public ResponseEntity<?> voteAnswer(@RequestBody AnswerVoteDto voteDto) {
        try {
            var result = answerService.voteAnswer(
                    voteDto.getUserId(),
                    voteDto.getAnswerId(),
                    voteDto.getVoteType()
            );

            if (result == null) {
                return ResponseEntity.ok("Vote supprimé");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du vote : " + e.getMessage());
        }
    }

    // ✅ Obtenir les stats des votes pour une réponse
    @GetMapping("/{answerId}/votes")
    public ResponseEntity<?> getAnswerVoteStats(@PathVariable int answerId) {
        return ResponseEntity.ok(answerService.getAnswerVoteStats(answerId));
    }

}
