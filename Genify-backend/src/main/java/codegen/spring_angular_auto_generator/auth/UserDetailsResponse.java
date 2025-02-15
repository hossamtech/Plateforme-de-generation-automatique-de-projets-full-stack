package codegen.spring_angular_auto_generator.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailsResponse {
    private String email;
    private String profileImg;
}