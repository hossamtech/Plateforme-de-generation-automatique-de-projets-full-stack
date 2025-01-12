package codegen.spring_angular_auto_generator.DiagramClassHistory.dto;

import codegen.spring_angular_auto_generator.DiagramClassHistory.PermissionType;
import codegen.spring_angular_auto_generator.DiagramClassHistory.ShareLinkVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionAndVisibilityShareLink {
    private PermissionType permissionType;
    private ShareLinkVisibility shareLinkVisibility;
}

