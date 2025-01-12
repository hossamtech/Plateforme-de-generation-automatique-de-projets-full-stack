package codegen.spring_angular_auto_generator.DiagramClassHistory.entities;

import codegen.spring_angular_auto_generator.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "diagram_class_history")
@EntityListeners(AuditingEntityListener.class)
public class DiagramClassHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String historyName;

    @Column(nullable = false)
    private String serial;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String diagramClassJson;

    @OneToOne(mappedBy = "diagramClassHistory")
    private ShareLink shareLink;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String associationsJson;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String codeEditor;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;
}
