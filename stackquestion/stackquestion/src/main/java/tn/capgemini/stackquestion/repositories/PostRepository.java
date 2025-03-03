package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
