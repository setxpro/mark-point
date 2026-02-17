package br.com.bytestorm.mark_point.security;

import br.com.bytestorm.mark_point.entity.dto.MessageDTO;
import br.com.bytestorm.mark_point.utils.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.InternalException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exception -> exception
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                                // Exemplo de mensagem de erro
                                String json = new ObjectMapper()
                                        .writeValueAsString(new MessageDTO("Usuário precisa estar autenticado para realizar esta ação!", HttpStatus.UNAUTHORIZED.value()));
                                response.getWriter().write(json);
                            })
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                                String json = new ObjectMapper()
                                        .writeValueAsString(new MessageDTO("Usuário não tem permissão para executar essa tarefa!", HttpStatus.FORBIDDEN.value()));
                                response.getWriter().write(json);
                            })
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorize ->
                            authorize
                                    // TODO: TEST ENDPOINTS
                                    .requestMatchers(HttpMethod.POST, Const.USER).permitAll()
                                    .requestMatchers(HttpMethod.PATCH, Const.USER + "/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, Const.USER + "/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, Const.USER).permitAll()
                                    .requestMatchers(HttpMethod.DELETE, Const.USER + "/**").permitAll()

                                    // TODO: TEST ENDPOINTS
                                    .requestMatchers(HttpMethod.POST, Const.COMPANY).permitAll()
                                    .requestMatchers(HttpMethod.PATCH, Const.COMPANY + "/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, Const.COMPANY + "/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, Const.COMPANY).permitAll()
                                    .requestMatchers(HttpMethod.DELETE, Const.COMPANY + "/**").permitAll()

                                    .requestMatchers(HttpMethod.POST, Const.AUTH + "/sign-in").permitAll()
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    .anyRequest().authenticated()
                    )
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        } catch (Exception e) {
            throw new InternalException("Erro Interno: " + e.getMessage());
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Crypt password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
