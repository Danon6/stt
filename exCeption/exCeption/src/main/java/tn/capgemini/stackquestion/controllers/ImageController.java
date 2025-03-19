package tn.capgemini.stackquestion.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.services.user.image.ImageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/image/{answerId}")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile multipartFile, @PathVariable int answerId ){
        try{
            imageService.storeFile(multipartFile, answerId);
            return ResponseEntity.ok("Image stored successfully");
        }catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}