package codegen.spring_angular_auto_generator.auth;

import codegen.spring_angular_auto_generator.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "Resource User")
public class ResourceController {

    private final AuthenticationService service;

    @GetMapping("/userDetails")
    public ResponseEntity<UserDetailsResponse> getUserDetails(
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.getUserDetails(connectedUser));
    }


}
