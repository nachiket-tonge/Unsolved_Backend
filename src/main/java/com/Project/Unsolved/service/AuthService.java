package com.Project.Unsolved.service;

import com.Project.Unsolved.dto.LoginRequestDto;
import com.Project.Unsolved.dto.SignUpRequestDto;
import com.Project.Unsolved.entity.BaseProfile;
import com.Project.Unsolved.entity.User;
import com.Project.Unsolved.repository.BaseProfileRepository;
import com.Project.Unsolved.repository.UserRepository;
import com.Project.Unsolved.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BaseProfileRepository baseProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       BaseProfileRepository baseProfileRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.baseProfileRepository = baseProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager ;
    }

    public void signUp(SignUpRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("User already exists with email: " + requestDto.getEmail());
        }

        // Create User (role NULL initially)
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
                .enabled(true)
                .build();
        User savedUser = userRepository.save(user);

        // ✅ Create FULL BaseProfile matching fakeAuth (empty fields)
        BaseProfile profile = BaseProfile.builder()
                .user(savedUser)
                .name(requestDto.getName())
                .city("")           // Matches fake: city: ""
                .state("")          // Matches fake: state: ""
                .bio("")            // Matches fake: bio: ""
                .profileImageUrl(null)  // Matches fake: photo: null
                .build();
        baseProfileRepository.save(profile);
    }

    // Helper methods for controller
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public BaseProfile getBaseProfileByUser(User user) {
        return baseProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public String login(LoginRequestDto requestDto) {
        // 1. Authenticate credentials using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );

        // 2. Fetch user details after successful authentication
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found: " + requestDto.getEmail()));

        // 3. Generate JWT token with userId and role (handle null role)
        String role = user.getRole() != null ? user.getRole().name() : "USER";
        return jwtUtil.generateToken(user.getId(), role);
    }
    public String signUpAndLogin(SignUpRequestDto dto) {
        signUp(dto); // existing signup logic
        return login(new LoginRequestDto(dto.getEmail(), dto.getPassword()));
    }


}
