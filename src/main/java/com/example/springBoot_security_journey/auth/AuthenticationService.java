package com.example.springBoot_security_journey.auth;
import com.example.springBoot_security_journey.config.JwtService;
import com.example.springBoot_security_journey.user.Role;
import com.example.springBoot_security_journey.user.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.springBoot_security_journey.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname()) // Ensure firstname field is set
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) // Assuming Role.USER is valid
                .build();
        userRepository.save(user);
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwToken).build(); // Placeholder response
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       request.getEmail(),
                       request.getPassword()
               )
       );
       var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow();
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwToken).build();
    }
}
