package codegen.spring_angular_auto_generator.auth;


import codegen.spring_angular_auto_generator.Token.Token;
import codegen.spring_angular_auto_generator.Token.TokenRepository;
import codegen.spring_angular_auto_generator.email.EmailService;
import codegen.spring_angular_auto_generator.email.EmailTemplateName;
import codegen.spring_angular_auto_generator.enums.UserRole;
import codegen.spring_angular_auto_generator.exception.ActivationTokenException;
import codegen.spring_angular_auto_generator.exception.EmailAlreadyExistsException;
import codegen.spring_angular_auto_generator.security.JwtService;
import codegen.spring_angular_auto_generator.user.User;
import codegen.spring_angular_auto_generator.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Value("${application.file.user-data.output-path}")
    private String userDataPath;

    @Value("${application.file.profile.output-path}")
    private String profileOutputPath;

    @Value("${application.file.downloads.output-path}")
    private String downloadsOutputPath;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException, IOException {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.USER)
                .profileImg(null)
                .downloads(null)
                .enabled(false)
                .accountLocked(false)
                .build();

        userRepository.save(user);

       createUserDirectories(user.getId());

        user.setProfileImg(userDataPath + "/user_" + user.getId() + profileOutputPath);
        user.setDownloads(userDataPath + "/user_" + user.getId() + downloadsOutputPath);
        userRepository.save(user);

        sendValidationEmail(user);
    }

    private void createUserDirectories(Long userId) throws IOException {
        String userFolderPath = userDataPath + "/user_" + userId;
        File userMainDir = new File(userFolderPath);

        if (!userMainDir.exists() && !userMainDir.mkdirs()) {
            throw new IOException("Failed to create main user directory: " + userMainDir.getAbsolutePath());
        }

        File profileDir = new File(userFolderPath + profileOutputPath);
        if (!profileDir.exists() && !profileDir.mkdirs()) {
            throw new IOException("Failed to create profile directory: " + profileDir.getAbsolutePath());
        }

        File downloadsDir = new File(userFolderPath + downloadsOutputPath);
        if (!downloadsDir.exists() && !downloadsDir.mkdirs()) {
            throw new IOException("Failed to create downloads directory: " + downloadsDir.getAbsolutePath());
        }
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken((User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public UserDetailsResponse getUserDetails(Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        return userRepository.findByEmail(user.getEmail())
                .map(userEntity -> UserDetailsResponse.builder()
                        .email(userEntity.getEmail())
                        .profileImg(ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/user/profile").toUriString()) //
                        // Assuming this path is accessible
                        .build())
                .orElseThrow(() -> new EntityNotFoundException("No user found with the email: " + user.getEmail()));

    }

    public void activateAccount(String token) throws MessagingException {
        if (token.equals("number")) {
            throw new ActivationTokenException("You will be enter the code sent in your email");
        }
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new ActivationTokenException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new ActivationTokenException("Activation token has expired. A new token has been send to the same email " +
                    "address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getEmail(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }


//    public UserRole mapRoleType(String roleType) {
//        return switch (roleType.toUpperCase()) {
//            case "ETUDIANT" -> UserRole.ETUDIANT;
//            case "ENCADRANT" -> UserRole.ENCADRANT;
//            default -> throw new IllegalArgumentException("Unknown role type: " + roleType);
//        };
//    }
}
