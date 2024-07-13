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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void signup(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new BodyGuardException("Username is already taken");
        }

        RoleEntity roleEntity = roleRepository.findRoleEntityByTitle(Roles.user.name())
                .orElseThrow(() -> new BodyGuardException("No roles found"));

        UserEntity user = new UserEntity();
        user.setUsername(signupRequest.getUsername().toLowerCase());
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        user.setRole(roleEntity);

        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        requiredNonNull(loginRequest.getUsername(), "username");
        requiredNonNull(loginRequest.getPassword(), "password");

        String username = loginRequest.getUsername().toLowerCase();
        String password = loginRequest.getPassword();

        authenticate(username, password);

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setId(userDetails.getId());
        response.setUsername(userDetails.getUsername());
        response.setRole(userDetails.getRole());
        response.setToken("Bearer " + token);

        return response;
    }

    private void requiredNonNull(Object obj, String name) {
        if (obj == null || obj.toString().isEmpty()) {
            throw new BodyGuardException(name + " cannot be empty");
        }
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BodyGuardException("Incorrect username or password");
        }
    }
}
