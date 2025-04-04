package tn.capgemini.stackquestion.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.stackquestion.dto.AllQuestionResponseDto;
import tn.capgemini.stackquestion.dto.QuestionDTO;
import tn.capgemini.stackquestion.dto.SingleQuestionDto;
import tn.capgemini.stackquestion.services.question.QuestionService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @PostMapping("/question")
    public ResponseEntity<?> postQuestion(@RequestBody QuestionDTO questionDto){
        QuestionDTO createdQuestionDto = questionService.addQuestion(questionDto);

        if (createdQuestionDto == null){
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestionDto);
    }

    @GetMapping("/questions/{pageNumber}")
    public ResponseEntity<AllQuestionResponseDto> getAllQuestions(@PathVariable int pageNumber){
        AllQuestionResponseDto allQuestionResponseDto = questionService.getAllQuestions(pageNumber);
        return ResponseEntity.ok(allQuestionResponseDto);
    }

    @GetMapping("/question/{userId}/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable int userId , @PathVariable int questionId){
        SingleQuestionDto singleQuestionDto = questionService.getQuestionById(userId, questionId);
        if (singleQuestionDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(singleQuestionDto);
    }

    @GetMapping("/questions/{userId}/{pageNumber}")
    public ResponseEntity<AllQuestionResponseDto> getQuestionsByUserId(@PathVariable int userId, @PathVariable int pageNumber){
        AllQuestionResponseDto allQuestionResponseDto = questionService.getAllQuestionsByUserId(userId, pageNumber);
        return ResponseEntity.ok(allQuestionResponseDto);
    }

}
