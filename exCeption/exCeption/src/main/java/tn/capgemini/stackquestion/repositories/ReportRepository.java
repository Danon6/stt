package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}
