package account.config;

import account.exception.CustomAccessDeniedHandler;
import account.exception.CustomAuthenticationEntryPoint;
import account.exception.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/security/events/").hasAnyRole("AUDITOR")
                .requestMatchers(HttpMethod.GET, "/api/admin/user/**").hasAnyRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasAnyRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/api/admin/user/**").hasAnyRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/api/admin/user/role").hasAnyRole("ADMINISTRATOR")
                .requestMatchers(HttpMethod.POST, "/api/acct/payments").hasAnyRole("ACCOUNTANT")
                .requestMatchers(HttpMethod.PUT, "/api/acct/payments").hasAnyRole("ACCOUNTANT")
                .requestMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
                .requestMatchers(HttpMethod.POST, "/api/auth/changepass").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/signup", "/actuator/shutdown").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        return http.build();
    }

}
