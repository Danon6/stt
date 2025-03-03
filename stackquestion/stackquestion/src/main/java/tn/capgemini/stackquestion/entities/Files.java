package tn.capgemini.stackquestion.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.capgemini.stackquestion.entities.enums.typeFile;

@Entity
@Data
@NoArgsConstructor
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer file_id;
    private String file_name;
    private typeFile file_type;
    private String file_size;
    private String file_url;
    @ManyToOne
    private Post post;
}
