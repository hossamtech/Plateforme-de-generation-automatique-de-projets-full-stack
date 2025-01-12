package codegen.spring_angular_auto_generator.DiagramClassHistory.repository;


import codegen.spring_angular_auto_generator.DiagramClassHistory.entities.DiagramClassHistory;
import codegen.spring_angular_auto_generator.DiagramClassHistory.entities.ShareLink;
import codegen.spring_angular_auto_generator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {

    Optional<ShareLink> findByUserAndDiagramClassHistory(User user, DiagramClassHistory diagramClassHistory);
    Optional<ShareLink> findByDiagramClassHistory(DiagramClassHistory diagramClassHistory);
}
