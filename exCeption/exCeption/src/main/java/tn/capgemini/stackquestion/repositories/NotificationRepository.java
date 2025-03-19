package tn.capgemini.exCeption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.capgemini.exCeption.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
