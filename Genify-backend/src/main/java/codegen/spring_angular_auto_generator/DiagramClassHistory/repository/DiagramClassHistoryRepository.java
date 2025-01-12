package codegen.spring_angular_auto_generator.DiagramClassHistory.repository;

import codegen.spring_angular_auto_generator.DiagramClassHistory.entities.DiagramClassHistory;
import codegen.spring_angular_auto_generator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagramClassHistoryRepository extends JpaRepository<DiagramClassHistory, Long> {
    DiagramClassHistory findByHistoryNameAndUser(String historyName, User user);
    List<DiagramClassHistory> findAllByUser(User user);
    Optional<DiagramClassHistory>  findByUserAndSerial(User user, String serial);
    Optional<DiagramClassHistory>  findBySerial(String serial);
}
