package KfhTask1.Task1.service.auth;

import KfhTask1.Task1.bo.CustomUserDetails;
import KfhTask1.Task1.entity.UserEntity;
import KfhTask1.Task1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserName(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setRole(user.getRole().getTitle().name());

        return userDetails;
    }
}
