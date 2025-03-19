package tn.capgemini.exCeption.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.capgemini.exCeption.dto.AnswerDto;
import tn.capgemini.exCeption.repositories.AnswerRepository;
import tn.capgemini.exCeption.services.answer.AnswerServiceImpl;

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
