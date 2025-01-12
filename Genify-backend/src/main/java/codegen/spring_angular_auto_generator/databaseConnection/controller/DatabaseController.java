package codegen.spring_angular_auto_generator.databaseConnection.controller;

import codegen.spring_angular_auto_generator.databaseConnection.dto.DatabaseRequest;
import codegen.spring_angular_auto_generator.databaseConnection.service.DatabaseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("database")
@AllArgsConstructor
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/mysql/test-connection")
    public ResponseEntity<Map<String, String>> testConnection(@Valid @RequestBody DatabaseRequest databaseRequest) {
        return databaseService.testConnection(databaseRequest);
    }
}
