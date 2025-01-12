package codegen.spring_angular_auto_generator.websocket;

import codegen.spring_angular_auto_generator.DiagramClassHistory.dto.HistoryRequest;
import codegen.spring_angular_auto_generator.DiagramClassHistory.dto.SaveHistoryResponse;
import codegen.spring_angular_auto_generator.DiagramClassHistory.services.DiagramClassHistoryService;
import codegen.spring_angular_auto_generator.handler.WebSocketHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DiagramWebSocketController {

    private final WebSocketHandler webSocketHandler;
    private final DiagramClassHistoryService diagramClassHistoryService;

    public DiagramWebSocketController(WebSocketHandler webSocketHandler, DiagramClassHistoryService diagramClassHistoryService) {
        this.webSocketHandler = webSocketHandler;
        this.diagramClassHistoryService = diagramClassHistoryService;
    }

//    @MessageMapping("/diagram.update")  // Endpoint for receiving update requests
//    public void sendUpdatedDiagram(SaveHistoryResponse saveHistoryResponse) {
//        // Debugging log to check the received message
//        System.out.println("Received update for serial: " + saveHistoryResponse.getSerial());
//
//        // Send the update to all connected clients
//        webSocketHandler.sendUpdatedDiagram(saveHistoryResponse);
//    }

}
