package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Departement;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
}
