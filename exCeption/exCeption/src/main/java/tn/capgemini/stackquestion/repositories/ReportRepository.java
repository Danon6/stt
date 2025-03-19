package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
