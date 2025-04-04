package tn.capgemini.stackquestion.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.capgemini.stackquestion.entities.Image;
import tn.capgemini.stackquestion.repositories.ImageRepository;
import tn.capgemini.stackquestion.services.user.image.ImageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;
    private final ImageRepository imageRepository;


    // ✅ Upload image for an Answer
    @PostMapping("/image/{answerId}")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("multipartFile") MultipartFile multipartFile,
            @PathVariable int answerId) {
        try {
            imageService.storeFile(multipartFile, answerId);
            return ResponseEntity.ok(Map.of("message", "Image stored successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/image/question/{questionId}")
    public ResponseEntity<Map<String, String>> uploadQuestionImage(
            @RequestParam("multipartFile") MultipartFile multipartFile,
            @PathVariable int questionId) {
        try {
            imageService.storeFileForQuestion(multipartFile, questionId);
            return ResponseEntity.ok(Map.of("message", "Image stored successfully"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/image/question/{questionId}")
    public ResponseEntity<byte[]> getImageByQuestionId(@PathVariable int questionId) {
        Image image = imageRepository.findByQuestionId(questionId);
        if (image != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", image.getType())
                    .body(image.getData());
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/image/answer/{answerId}")
    public ResponseEntity<byte[]> getImageByAnswerId(@PathVariable int answerId) {
        try {
            Image image = imageRepository.findByAnswerId(answerId);

            if (image == null || image.getData() == null) {
                System.out.println("❌ No image found for answer ID: " + answerId);
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Type", image.getType())
                    .header("Content-Disposition", "inline; filename=\"" + image.getName() + "\"")
                    .body(image.getData());

        } catch (Exception e) {
            System.err.println("❌ Error while fetching image for answer ID " + answerId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
