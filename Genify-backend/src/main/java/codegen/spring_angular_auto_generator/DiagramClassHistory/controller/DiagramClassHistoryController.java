package codegen.spring_angular_auto_generator.DiagramClassHistory.controller;


import codegen.spring_angular_auto_generator.DiagramClassHistory.dto.*;
import codegen.spring_angular_auto_generator.DiagramClassHistory.entities.DiagramClassHistory;
import codegen.spring_angular_auto_generator.DiagramClassHistory.services.DiagramClassHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class DiagramClassHistoryController {

    private final DiagramClassHistoryService historyService;


    @PostMapping("/save")
    public ResponseEntity<SaveHistoryResponse> saveHistory(
            @RequestBody HistoryRequest historyRequest,
            Authentication connectedUser
    ) throws Exception {
        return ResponseEntity.ok(historyService.saveOrUpdateHistory(connectedUser, historyRequest));
    }

    @PostMapping("/save/sharingDiagram")
    public ResponseEntity<SaveHistoryResponse> saveSharingDiagram(
            @RequestBody HistoryRequest historyRequest
    ) throws Exception {
        return ResponseEntity.ok(historyService.saveSharingDiagram(historyRequest));
    }

    @GetMapping("/{serial}")
    public ResponseEntity<HistoryResponse> loadHistoryOrShareHistoryFromAuthorizedUserBySerial(@PathVariable String serial,
                                                                                   Authentication connectedUser) throws Exception {
        HistoryResponse history = historyService.loadHistoryOrShareHistoryFromAuthorizedUserBySerial(connectedUser, serial);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{serial}/sharing")
    public ResponseEntity<HistoryResponse> loadShareHistoryFromUnauthorizedUserBySerial(
            @PathVariable String serial) {
        HistoryResponse history = historyService.loadShareHistoryFromAuthorizedUserBySerial(serial);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/user")
    public List<ListHistoriesResponse> getListHistoriesForUser(Authentication connectedUser) {
        return historyService.getListHistoriesForUser(connectedUser);
    }

    @GetMapping("{serial}/PermissionAndVisibility/shareLink")
    public ResponseEntity<PermissionAndVisibilityShareLink> getPermissionAndVisibilityShareLink(
            @PathVariable String serial,
            Authentication connectedUser
    ) throws Exception {
        return ResponseEntity.ok(historyService.getPermissionAndVisibilityShareLink(connectedUser, serial));
    }

    @PutMapping("/{serial}/share-link/permission")
    public ResponseEntity<ShareLinkResponse> updateVisibilityAndPermission(
            @PathVariable String serial,
            @RequestBody PermissionAndVisibilityShareLink request,
            Authentication connectedUser

    ) {
        return ResponseEntity.ok(historyService.updateVisibilityAndPermission(serial, connectedUser, request));
    }

    @DeleteMapping("/{serial}/deleteHistory")
    public ResponseEntity<Boolean> deleteHistory(
            @PathVariable String serial,
            Authentication authenticatedUser) {
        try {
            historyService.deleteHistory(authenticatedUser, serial);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }
}

