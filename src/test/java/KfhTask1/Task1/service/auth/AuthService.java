package KfhTask1.Task1.service.auth;

import KfhTask1.Task1.bo.auth.AuthenticationResponse;
import KfhTask1.Task1.bo.auth.LoginRequest;
import KfhTask1.Task1.bo.auth.SignupRequest;

public interface AuthService {
    void signup(SignupRequest createSignupRequest);

    AuthenticationResponse login(LoginRequest createLoginRequest);
}
