package account.config;

import account.exception.CustomAccessDeniedHandler;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.text.SimpleDateFormat;


@Configuration
public class ApplicationConfig {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ApplicationConfig(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }



    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SimpleDateFormat dateFormat() {
        return new SimpleDateFormat();
    }

}
