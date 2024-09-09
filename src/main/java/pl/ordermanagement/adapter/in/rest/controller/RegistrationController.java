package pl.ordermanagement.adapter.in.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.ordermanagement.adapter.in.rest.model.RegisterUserRequest;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUserAccount(@RequestBody RegisterUserRequest request) {
        org.springframework.security.core.userdetails.UserDetails user =
                org.springframework.security.core.userdetails.User.builder()
                        .username(request.username())
                        .password(passwordEncoder.encode(request.password()))
                        .roles(request.role())
                        .build();

        jdbcUserDetailsManager.createUser(user);
        return "User has been successfully registered. Try to log in.";
    }
}
