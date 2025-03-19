package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.capgemini.exCeption.entities.Answer;
import tn.capgemini.exCeption.entities.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByAnswer(Answer answer);
}
