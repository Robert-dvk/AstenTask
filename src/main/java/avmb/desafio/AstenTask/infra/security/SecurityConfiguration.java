package avmb.desafio.AstenTask.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final SecurityFilter securityFilter;
    private final AuthEntryPointError authEntryPointError;
    private final AccessDeniedError accessDeniedError;

    public SecurityConfiguration(SecurityFilter securityFilter, AuthEntryPointError authEntryPointError, AccessDeniedError accessDeniedError) {
        this.securityFilter = securityFilter;
        this.authEntryPointError = authEntryPointError;
        this.accessDeniedError = accessDeniedError;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")
                        .requestMatchers(HttpMethod.DELETE, "/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/projects/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.POST, "/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")
                        .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/tasks/**").hasAnyRole("ADMIN", "PROJECT_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.PUT, "/comments/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.DELETE, "/time-logs/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.PUT, "/time-logs/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER")
                        .requestMatchers(HttpMethod.GET, "/dashboard/**").hasAnyRole("ADMIN", "PROJECT_MANAGER", "DEVELOPER", "VIEWER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authEntryPointError)
                    .accessDeniedHandler(accessDeniedError)
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
