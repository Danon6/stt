package tn.capgemini.stackquestion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import tn.capgemini.stackquestion.dto.KnowledgeDTO;
import tn.capgemini.stackquestion.entities.enums.VoteType;

import java.util.Base64;
import java.util.Date;

@Entity
@Data
@Table(name = "knowledge")
public class Knowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private String departement;
    private String projet;


    @Lob
    private String content;

    @Lob
    private byte[] image;

    private String imageType; // e.g., image/png, image/jpeg
    @Lob
    private byte[] file;
    private String fileType;
    @CreationTimestamp
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "knowledge", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<KnowledgeVote> votes = new java.util.ArrayList<>();

    public int getVoteScore() {
        return (int) votes.stream()
                .mapToInt(v -> v.getVoteType() == VoteType.UPVOTE ? 1 : -1)
                .sum();
    }

    public KnowledgeDTO toDTO() {
        KnowledgeDTO dto = new KnowledgeDTO();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setDescription(this.description);
        dto.setContent(this.content);
        if (this.image != null) {
            dto.setImageBase64("data:" + this.imageType + ";base64," + Base64.getEncoder().encodeToString(this.image));
            dto.setImageType(this.imageType);
        }
        if (this.file != null) {
            dto.setFileBase64("data:" + this.fileType + ";base64," + Base64.getEncoder().encodeToString(this.file));
            dto.setFileType(this.fileType);
        }

        dto.setUserId(this.user.getUser_id());
        dto.setUserName(this.user.getName());
        dto.setCreatedDate(this.createdDate);
        dto.setVoteCount(this.getVoteScore());
        dto.setDepartement(this.getDepartement());
        dto.setProjet(this.getProjet());

        return dto;
    }

}
