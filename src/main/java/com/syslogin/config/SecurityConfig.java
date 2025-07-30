package com.syslogin.config;

import com.syslogin.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    // Assumindo que jwtTokenFilter é injetado ou definido em outro lugar
    // Ex: private final JwtTokenFilter jwtTokenFilter;
    // Se for um bean, o Spring vai injetá-lo automaticamente.
    // Se não for um bean e você o instanciar aqui, pode precisar de um construtor.
    // Por exemplo, se JwtTokenFilter for um @Component ou @Service
    // @Autowired
    // private JwtTokenFilter jwtTokenFilter;

    // Apenas para fins de exemplo, se jwtTokenFilter não for um bean gerenciado pelo Spring
    // Remova ou ajuste conforme sua implementação real

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita e configura CORS usando a fonte de configuração definida abaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST (geralmente recomendado)
                .authorizeRequests(auth -> auth
                        // Usando antMatchers para especificar múltiplos padrões de URL
                        // IMPORTANTE: Estes caminhos são os *internos* após o context-path ser removido.
                        // Então, /api/auth/login se torna /auth/login aqui.
                        .antMatchers("/user/register", "/user/recover-password", "/auth/login", "/user/new-password", "/**").permitAll() // '/**' para cobrir todas as rotas internas
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Define a fonte de configuração CORS que será usada pelo Spring Security
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ATENÇÃO: Usar "*" (todos os domínios) é APENAS para DEPURAR e NUNCA deve ser usado em PRODUÇÃO.
        // Isso nos ajudará a confirmar se o problema é a origem.
        // Se funcionar, você precisará voltar para origens específicas.
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://127.0.0.1:8081"));
        // Define os métodos HTTP permitidos.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Define os cabeçalhos permitidos.
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permite o envio de credenciais (cookies, cabeçalhos de autorização).
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // NOVO: Registra a configuração CORS para '/**'.
        // Isso cobrirá TODAS as requisições após o context-path ser removido,
        // garantindo que o CORS seja aplicado a /auth/login e qualquer outra API interna.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
