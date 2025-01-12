package codegen.spring_angular_auto_generator.databaseConnection.service;

import codegen.spring_angular_auto_generator.databaseConnection.dto.DatabaseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DatabaseService {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public ResponseEntity<Map<String, String>> testConnection(DatabaseRequest databaseRequest) {
        String jdbcUrl = "jdbc:mysql://localhost:" + databaseRequest.getPort() + "/" + databaseRequest.getDataBaseName();
        Map<String, String> responseMessage = new HashMap<>();

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(jdbcUrl, databaseRequest.getUsername(), databaseRequest.getPassword())) {
                if (connection != null) {
                    responseMessage.put("message", "Connection to database '" + databaseRequest.getDataBaseName() + "' was successful.");
                    return ResponseEntity.ok(responseMessage);
                } else {
                    responseMessage.put("message", "Failed to connect to the database.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
                }
            }
        } catch (ClassNotFoundException e) {
            responseMessage.put("message", "JDBC Driver not found.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        } catch (SQLException e) {
            responseMessage.put("message", "Failed to connect to the database");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

}
