package tn.capgemini.stackquestion.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.capgemini.stackquestion.entities.Knowledge;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Integer> {
    @Query("SELECT k FROM Knowledge k WHERE k.user.user_id = :userId")
    Page<Knowledge> findAllByUserId(@Param("userId") Integer userId, Pageable pageable);
}
