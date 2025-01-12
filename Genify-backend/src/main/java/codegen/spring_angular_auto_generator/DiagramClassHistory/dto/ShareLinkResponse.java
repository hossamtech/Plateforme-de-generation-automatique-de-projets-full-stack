package codegen.spring_angular_auto_generator.DiagramClassHistory.dto;

import codegen.spring_angular_auto_generator.DiagramClassHistory.PermissionType;
import codegen.spring_angular_auto_generator.DiagramClassHistory.ShareLinkVisibility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShareLinkResponse {
    private String ownerEmail;
    private PermissionType permissionType;
    private ShareLinkVisibility shareLinkVisibility;
}
