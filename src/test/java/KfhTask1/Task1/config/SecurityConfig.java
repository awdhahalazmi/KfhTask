package KfhTask1.Task1.config;

import KfhTask1.Task1.service.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTH_PATH = "/api/auth/**";
    public static final String DB_PATH = "/h2-console/**";
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "PUT", "POST", "DELETE", "OPTIONS", "PATCH");
    private static final List<String> ALLOWED_HEADERS = Arrays.asList("x-requested-with", "authorization", "Content-Type",
            "Authorization", "credential", "X-XSRF-TOKEN", "X-Refresh-Token", "X-Client-Id", "x-client-id");

    private JWTUtil jwtUtil;

    private CustomUserDetailsService userDetailsService;


    private JWTAuthFilter jwtAuthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Enable sessions
                .and()
                .cors()
                .configurationSource(request -> getCorsConfiguration())
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_PATH).permitAll()
                .antMatchers(DB_PATH).permitAll() // Allow access to H2 console
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/configuration/**", "/webjars/**", "/favicon-**").permitAll()
                .anyRequest().authenticated();

        http.headers().frameOptions().sameOrigin(); // Allow frames from the same origin for H2 console

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfiguration.setAllowedMethods(ALLOWED_METHODS);
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);

        return corsConfiguration;
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
