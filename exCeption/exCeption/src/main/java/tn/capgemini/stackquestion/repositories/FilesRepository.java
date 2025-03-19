package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Files;

public interface FilesRepository extends JpaRepository<Files, Long> {
}
