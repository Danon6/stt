package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Comment;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
}
