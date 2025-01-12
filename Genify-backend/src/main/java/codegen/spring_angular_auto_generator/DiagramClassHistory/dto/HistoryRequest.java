package codegen.spring_angular_auto_generator.DiagramClassHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryRequest {
    private String historyName;
    private String codeEditor;
    private String diagramsClass;
    private String associations;
    private String serial;
}
