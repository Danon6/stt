package tn.capgemini.stackquestion.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.capgemini.stackquestion.dto.QuestionVoteDto;
import tn.capgemini.stackquestion.entities.Question;
import tn.capgemini.stackquestion.services.question.QuestionService;
import tn.capgemini.stackquestion.services.question.QuestionServiceImpl;
import tn.capgemini.stackquestion.services.vote.VoteService;
import tn.capgemini.stackquestion.services.vote.VoteServiceImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VoteController {

    @Autowired
    VoteServiceImpl voteService;
    @Autowired
    QuestionServiceImpl questionService;

    @PostMapping("/vote")
    public ResponseEntity<?> addVoteToQuestion(@RequestBody QuestionVoteDto question){
        QuestionVoteDto questionVotedDto = voteService.addVoteToQuestion(question);
        if(questionVotedDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!");
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body(questionVotedDto);
        }

    }


}
