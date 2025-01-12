package codegen.spring_angular_auto_generator.handler;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    // Store active sessions by serial number (multiple sessions for the same serial)
    private final Map<String, Map<String, WebSocketSession>> activeSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Extract the serial number from the session URI
        String serial = getSerialFromSession(session);
        if (serial != null) {
            // If the serial already exists, add the new session to it
            activeSessions.computeIfAbsent(serial, k -> new HashMap<>()).put(session.getId(), session);
            logger.info("Connection established with serial: " + serial);
        } else {
            logger.warn("Serial not found in the connection request.");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Extract the payload (message) from the TextMessage object
        String payload = message.getPayload();
        logger.info("Received message: " + payload);

        try {
            // Optionally, you can validate the format of the message, for example, parsing JSON
            JSONObject json = new JSONObject(payload);  // Assuming JSON message
            String serial = json.optString("serial");  // Assuming the serial is in the payload

            if (serial != null && !serial.isEmpty()) {
                // Send the message to all sessions with the matching serial
                Map<String, WebSocketSession> sessions = activeSessions.get(serial);
                for (WebSocketSession activeSession : sessions.values()) {
                    if (activeSession.isOpen() && !session.equals(activeSession)) {
                        activeSession.sendMessage(new TextMessage("Diagram updated: " + payload));
                    }
                }
            }
        } catch (JSONException e) {
            // Correcting the error logging
            logger.error("Error parsing message as JSON", e);  // Log the exception as the second parameter
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session from active sessions when it closes
        String serial = getSerialFromSession(session);
        if (serial != null && activeSessions.containsKey(serial)) {
            activeSessions.get(serial).remove(session.getId());
            if (activeSessions.get(serial).isEmpty()) {
                activeSessions.remove(serial);
            }
            logger.info("Connection closed for serial: " + serial);
        }
    }

    // Helper method to extract the serial number from the session URI
    private String getSerialFromSession(WebSocketSession session) {
        String uri = session.getUri().toString();
        String[] parts = uri.split("\\?");
        if (parts.length > 1) {
            String query = parts[1];
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "serial".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }

    // Optionally, you could parse the serial from the message payload as well
    // private String extractSerialFromPayload(String payload) {
    //     // Parse the payload (assuming it's JSON) and extract the serial
    // }
}
