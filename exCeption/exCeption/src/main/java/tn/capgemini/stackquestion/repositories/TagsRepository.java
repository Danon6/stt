package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.Tags;

public interface TagsRepository extends JpaRepository<Tags, Integer> {
}
