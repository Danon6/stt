package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Tags;

public interface TagsRepository extends JpaRepository<Tags, Long> {
}
