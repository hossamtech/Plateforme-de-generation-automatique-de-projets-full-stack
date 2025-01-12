package codegen.spring_angular_auto_generator.DiagramClassHistory.dto;

import codegen.spring_angular_auto_generator.DiagramClassHistory.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResponse {
    private HistoryRequest historyRequest;
    private ShareLinkResponse shareLinkResponse;
}
