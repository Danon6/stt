package tn.capgemini.stackquestion.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.stackquestion.dto.AnswerDto;
import tn.capgemini.stackquestion.repositories.AnswerRepository;
import tn.capgemini.stackquestion.services.answer.AnswerService;
import tn.capgemini.stackquestion.services.answer.AnswerServiceImpl;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    AnswerServiceImpl answerService;

    @Autowired
    AnswerRepository answerRepository;

    @PostMapping
    public ResponseEntity<?> addAnswer(@RequestBody AnswerDto answerDto){
        AnswerDto createdAnswerDto = answerService.postAnswer(answerDto);
        if (createdAnswerDto == null){
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswerDto);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllAnswers(){
        return  ResponseEntity.status(HttpStatus.OK).body(answerRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllAnswersByUserId(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(answerRepository.findAll());
    }



}
