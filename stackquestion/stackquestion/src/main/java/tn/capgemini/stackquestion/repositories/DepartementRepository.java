package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.Departement;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
}
