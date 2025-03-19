package tn.capgemini.stackquestion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.stackquestion.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
