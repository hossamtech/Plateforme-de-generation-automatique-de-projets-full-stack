package codegen.spring_angular_auto_generator.DiagramClassHistory.entities;

import codegen.spring_angular_auto_generator.DiagramClassHistory.PermissionType;
import codegen.spring_angular_auto_generator.DiagramClassHistory.ShareLinkVisibility;
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
@Table(name = "share_links")
@EntityListeners(AuditingEntityListener.class)
public class ShareLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "history_id", nullable = false)
    private DiagramClassHistory diagramClassHistory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionType permissionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShareLinkVisibility shareLinkVisibility;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;
}
