package tn.capgemini.stackquestion.entities;

import jakarta.persistence.*;
import lombok.Data;
import tn.capgemini.stackquestion.entities.enums.VoteType;

@Entity
@Data
@Table(name = "knowledge_votes", uniqueConstraints = @UniqueConstraint(columnNames = {"knowledge_id", "user_id"}))
public class KnowledgeVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VoteType voteType; // UPVOTE or DOWNVOTE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knowledge_id", nullable = false)
    private Knowledge knowledge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
