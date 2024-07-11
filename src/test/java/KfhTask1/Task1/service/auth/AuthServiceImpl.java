package KfhTask1.Task1.service.auth;

import KfhTask1.Task1.bo.CustomUserDetails;
import KfhTask1.Task1.bo.auth.AuthenticationResponse;
import KfhTask1.Task1.bo.auth.LoginRequest;
import KfhTask1.Task1.bo.auth.SignupRequest;
import KfhTask1.Task1.config.JWTUtil;
import KfhTask1.Task1.entity.RoleEntity;
import KfhTask1.Task1.entity.UserEntity;
import KfhTask1.Task1.repository.RoleRepository;
import KfhTask1.Task1.repository.UserRepository;
import KfhTask1.Task1.util.enums.Roles;
import KfhTask1.Task1.util.exceptions.BodyGuardException;
import KfhTask1.Task1.util.exceptions.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailService;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailService, JWTUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void signup(SignupRequest createSignupRequest) {
        if (userRepository.findByUsername(createSignupRequest.getUsername()).isPresent()) {
            throw new BodyGuardException("Username is already taken");
        }

        RoleEntity roleEntity = roleRepository.findRoleEntityByTitle(Roles.user.name())
                .orElseThrow(() -> new BodyGuardException("No roles found"));

        UserEntity user = new UserEntity();
        user.setUsername(createSignupRequest.getUsername().toLowerCase());
        user.setPassword(bCryptPasswordEncoder.encode(createSignupRequest.getPassword()));
        user.setRole(roleEntity);

        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest createLoginRequest) {
        requiredNonNull(createLoginRequest.getUsername(), "username");
        requiredNonNull(createLoginRequest.getPassword(), "password");

        String username = createLoginRequest.getUsername().toLowerCase();
        String password = createLoginRequest.getPassword();

        authentication(username, password);

        CustomUserDetails userDetails = userDetailService.loadUserByUsername(username);
        String accessToken = jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setId(userDetails.getId());
        response.setUsername(userDetails.getUsername());
        response.setRole(userDetails.getRole());
        response.setToken("Bearer " + accessToken);

        return response;
    }

    private void requiredNonNull(Object obj, String name) {
        if (obj == null || obj.toString().isEmpty()) {
            throw new BodyGuardException(name + " cannot be empty");
        }
    }

    private void authentication(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BodyGuardException("Incorrect username or password");
        }
    }
}
