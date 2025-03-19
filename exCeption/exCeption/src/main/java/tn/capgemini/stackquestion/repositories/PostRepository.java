package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
