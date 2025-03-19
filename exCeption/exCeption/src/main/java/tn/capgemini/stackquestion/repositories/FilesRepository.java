package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.Files;

public interface FilesRepository extends JpaRepository<Files, Integer> {
}
