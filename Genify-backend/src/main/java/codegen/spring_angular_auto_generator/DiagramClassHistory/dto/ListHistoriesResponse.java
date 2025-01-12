package codegen.spring_angular_auto_generator.DiagramClassHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListHistoriesResponse {
    private String serial;
    private String historyName;
    private String createdDate;
}
