package codegen.spring_angular_auto_generator.DiagramClassHistory.services;


import codegen.spring_angular_auto_generator.DiagramClassHistory.PermissionType;
import codegen.spring_angular_auto_generator.DiagramClassHistory.ShareLinkVisibility;
import codegen.spring_angular_auto_generator.DiagramClassHistory.dto.*;
import codegen.spring_angular_auto_generator.DiagramClassHistory.entities.DiagramClassHistory;
import codegen.spring_angular_auto_generator.DiagramClassHistory.entities.ShareLink;
import codegen.spring_angular_auto_generator.DiagramClassHistory.repository.DiagramClassHistoryRepository;
import codegen.spring_angular_auto_generator.DiagramClassHistory.repository.ShareLinkRepository;
import codegen.spring_angular_auto_generator.exception.UserHistoryException;
import codegen.spring_angular_auto_generator.user.User;
import codegen.spring_angular_auto_generator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiagramClassHistoryService {

    private final DiagramClassHistoryRepository historyRepository;
    private final ShareLinkRepository shareLinkRepository;
    private final UserRepository userRepository;

    public SaveHistoryResponse saveOrUpdateHistory(HistoryRequest historyRequest) {
        String serial = historyRequest.getSerial();
        DiagramClassHistory diagramClassHistory;

        // Check if the serial exists or create a new one
        if (serial == null || serial.isEmpty()) {
            String newSerial = generateNewSerial();  // Implement generateNewSerial() to generate a new serial

            // Create a new DiagramClassHistory instance
            diagramClassHistory = DiagramClassHistory.builder()
                    .historyName(historyRequest.getHistoryName())
                    .serial(newSerial)
                    .codeEditor(historyRequest.getCodeEditor())
                    .diagramClassJson(historyRequest.getDiagramsClass())
                    .associationsJson(historyRequest.getAssociations())
                    .build();

            diagramClassHistory = historyRepository.save(diagramClassHistory);

        } else {
            // If the serial exists, update the existing DiagramClassHistory
            diagramClassHistory = historyRepository.findBySerial(serial)
                    .orElseThrow(() -> new RuntimeException("History with the provided serial does not exist."));

            diagramClassHistory.setHistoryName(historyRequest.getHistoryName());
            diagramClassHistory.setCodeEditor(historyRequest.getCodeEditor());
            diagramClassHistory.setDiagramClassJson(historyRequest.getDiagramsClass());
            diagramClassHistory.setAssociationsJson(historyRequest.getAssociations());

            diagramClassHistory = historyRepository.save(diagramClassHistory);
        }

        // Return the response object with the serial
        return SaveHistoryResponse.builder()
                .serial(diagramClassHistory.getSerial())
                .build();
    }


    public SaveHistoryResponse saveOrUpdateHistory(Authentication connectedUser, HistoryRequest historyRequest) throws Exception {
        User user = (User) connectedUser.getPrincipal();

        DiagramClassHistory diagramClassHistory;
        String serial = historyRequest.getSerial();

        if (serial == null || serial.isEmpty()) {
            String newSerial = generateNewSerial();
            

            diagramClassHistory = DiagramClassHistory.builder()
                    .historyName(historyRequest.getHistoryName())
                    .serial(newSerial)
                    .codeEditor(historyRequest.getCodeEditor())
                    .diagramClassJson(historyRequest.getDiagramsClass())
                    .associationsJson(historyRequest.getAssociations())
                    .user(user)
                    .build();

            diagramClassHistory = historyRepository.save(diagramClassHistory);
            this.createShareLink(user, diagramClassHistory);

        } else {

            Optional<DiagramClassHistory> existingHistoryOpt = historyRepository.findByUserAndSerial(user, serial);

            if (existingHistoryOpt.isPresent()) {
                diagramClassHistory = existingHistoryOpt.get();

                diagramClassHistory.setHistoryName(historyRequest.getHistoryName());
                diagramClassHistory.setCodeEditor(historyRequest.getCodeEditor());
                diagramClassHistory.setUser(user);
                diagramClassHistory.setDiagramClassJson(historyRequest.getDiagramsClass());
                diagramClassHistory.setAssociationsJson(historyRequest.getAssociations());

                diagramClassHistory = historyRepository.save(diagramClassHistory);
            } else {
                throw new UserHistoryException("History with the provided serial does not exist.");
            }
        }

        return SaveHistoryResponse.builder()
                .serial(diagramClassHistory.getSerial())
                .build();
    }

    private String generateNewSerial() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public HistoryResponse loadHistoryOrShareHistoryFromAuthorizedUserBySerial(Authentication connectedUser, String serial) {
        User user = (User) connectedUser.getPrincipal();

        DiagramClassHistory history = historyRepository.findBySerial(serial)
                .orElseThrow(() -> new UserHistoryException("History entry not found with serial: " + serial));

        if (history.getUser().getId().equals(user.getId())) {
            return HistoryResponse.builder()
                    .historyRequest(HistoryRequest.builder()
                            .historyName(history.getHistoryName())
                            .diagramsClass(history.getDiagramClassJson())
                            .associations(history.getAssociationsJson())
                            .codeEditor(history.getCodeEditor())
                            .build())
                    .shareLinkResponse(null)
                    .build();
        } else {
            ShareLink shareLink = shareLinkRepository.findByUserAndDiagramClassHistory(history.getUser(), history)
                    .orElseThrow(() -> new RuntimeException("No share link found for the user and history entry"));

            ShareLinkResponse shareLinkResponse = ShareLinkResponse.builder()
                    .ownerEmail(history.getUser().getEmail())
                    .permissionType(shareLink.getPermissionType())
                    .shareLinkVisibility(shareLink.getShareLinkVisibility())
                    .build();

            return HistoryResponse.builder()
                    .historyRequest(HistoryRequest.builder()
                            .historyName(history.getHistoryName())
                            .diagramsClass(history.getDiagramClassJson())
                            .associations(history.getAssociationsJson())
                            .codeEditor(history.getCodeEditor())
                            .build())
                    .shareLinkResponse(shareLinkResponse)
                    .build();
        }
    }

    public HistoryResponse loadShareHistoryFromAuthorizedUserBySerial(String serial) {

        DiagramClassHistory history = historyRepository.findBySerial(serial)
                .orElseThrow(() -> new UserHistoryException("History entry not found with serial: " + serial));


        ShareLink shareLink = shareLinkRepository.findByDiagramClassHistory(history)
                .orElseThrow(() -> new RuntimeException("No share link found for the user and history entry"));

        ShareLinkResponse shareLinkResponse = ShareLinkResponse.builder()
                .ownerEmail(history.getUser().getEmail())
                .permissionType(shareLink.getPermissionType())
                .shareLinkVisibility(shareLink.getShareLinkVisibility())
                .build();

        return HistoryResponse.builder()
                .historyRequest(HistoryRequest.builder()
                        .historyName(history.getHistoryName())
                        .diagramsClass(history.getDiagramClassJson())
                        .associations(history.getAssociationsJson())
                        .codeEditor(history.getCodeEditor())
                        .build())
                .shareLinkResponse(shareLinkResponse)
                .build();

    }

    public List<ListHistoriesResponse> getListHistoriesForUser(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        List<DiagramClassHistory> histories = historyRepository.findAllByUser(user);
        return histories.stream()
                .map(history -> new ListHistoriesResponse(
                        history.getSerial(),
                        history.getHistoryName(),
                        formatDate(history.getCreatedDate())
                ))
                .collect(Collectors.toList());
    }

    private String formatDate(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);

        if (dateTime.toLocalDate().equals(now.toLocalDate())) {
            return "Today at " + dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        } else if (dateTime.toLocalDate().equals(yesterday.toLocalDate())) {
            return "Yesterday";
        }  else if (dateTime.getYear() == now.getYear()) {
            return dateTime.format(DateTimeFormatter.ofPattern("MMMM dd, hh:mm a"));
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a"));
        }
    }



    public void createShareLink(User user, DiagramClassHistory history) throws IllegalAccessException {

        ShareLink existingShareLink = shareLinkRepository.findByUserAndDiagramClassHistory(user, history)
                .orElse(null);

        if (existingShareLink == null) {
             
            ShareLink newShareLink = ShareLink.builder()
                    .user(user)
                    .diagramClassHistory(history)
                    .permissionType(PermissionType.CAN_VIEW)
                    .shareLinkVisibility(ShareLinkVisibility.PUBLIC)
                    .createdDate(LocalDateTime.now())
                    .build();

            shareLinkRepository.save(newShareLink);
            
        }
    }


    public SaveHistoryResponse saveSharingDiagram(HistoryRequest historyRequest) throws Exception {
        DiagramClassHistory diagramClassHistory;
        String serial = historyRequest.getSerial();

        Optional<DiagramClassHistory> existingHistoryOpt = historyRepository.findBySerial(serial);

        if (existingHistoryOpt.isPresent()) {
            diagramClassHistory = existingHistoryOpt.get();


            Optional<ShareLink> shareLinkOpt = shareLinkRepository.findByUserAndDiagramClassHistory(
                    diagramClassHistory.getUser(), diagramClassHistory
            );

            if (shareLinkOpt.isPresent()) {
                ShareLink shareLink = shareLinkOpt.get();


                if (shareLink.getPermissionType() != PermissionType.CAN_EDIT) {
                    throw new Exception("You do not have permission to save this diagram.");
                }

                diagramClassHistory.setHistoryName(historyRequest.getHistoryName());
                diagramClassHistory.setCodeEditor(historyRequest.getCodeEditor());
                diagramClassHistory.setDiagramClassJson(historyRequest.getDiagramsClass());
                diagramClassHistory.setAssociationsJson(historyRequest.getAssociations());

                diagramClassHistory = historyRepository.save(diagramClassHistory);

            } else {
                throw new Exception("No share link found for the user and history entry.");
            }
        } else {
            throw new Exception("History with the provided serial does not exist.");
        }

        return SaveHistoryResponse.builder()
                .serial(diagramClassHistory.getSerial())
                .build();
    }


    public PermissionAndVisibilityShareLink getPermissionAndVisibilityShareLink(Authentication connectedUser, String serial) throws Exception {
        User user = (User) connectedUser.getPrincipal();

        DiagramClassHistory history = historyRepository.findBySerial(serial)
                .orElseThrow(() -> new RuntimeException("History entry not found with serial: " + serial));
        if (history.getUser().getId().equals(user.getId())) {
            ShareLink shareLink = shareLinkRepository.findByUserAndDiagramClassHistory(user, history)
                    .orElseThrow(() -> new RuntimeException("No share link found for the user and history entry"));

            return PermissionAndVisibilityShareLink.builder()
                    .permissionType(shareLink.getPermissionType())
                    .shareLinkVisibility(shareLink.getShareLinkVisibility())
                    .build();
        }
        else {
            throw new Exception("teh share link is not associated with the user connected .");
        }
    }

    public ShareLinkResponse updateVisibilityAndPermission(String serial, Authentication connectedUser, PermissionAndVisibilityShareLink request) {
        User user = (User) connectedUser.getPrincipal();

        DiagramClassHistory history = historyRepository.findByUserAndSerial(user, serial)
                .orElseThrow(() -> new RuntimeException("History entry not found with serial: " + serial));

            ShareLink shareLink = shareLinkRepository.findByUserAndDiagramClassHistory(user, history)
                    .orElseThrow(() -> new RuntimeException("No share link found for the user and history entry"));

            shareLink.setPermissionType(request.getPermissionType());
            shareLink.setShareLinkVisibility(request.getShareLinkVisibility());
            ShareLink shareLinkUpdated  = shareLinkRepository.save(shareLink);

            return ShareLinkResponse.builder()
                    .permissionType(shareLinkUpdated.getPermissionType())
                    .shareLinkVisibility(shareLinkUpdated.getShareLinkVisibility())
                    .build();
    }

    public void deleteHistory(Authentication connectedUser, String serial) throws Exception {
        User user = (User) connectedUser.getPrincipal();

        DiagramClassHistory history = historyRepository.findByUserAndSerial(user, serial)
                .orElseThrow(() -> new RuntimeException("History entry not found with serial: " + serial));

        Optional<ShareLink> shareLinkOpt = shareLinkRepository.findByDiagramClassHistory(history);
        shareLinkOpt.ifPresent(shareLinkRepository::delete);

        historyRepository.delete(history);
    }

}
