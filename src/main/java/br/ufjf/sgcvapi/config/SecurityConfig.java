package br.ufjf.sgcvapi.config;

import br.ufjf.sgcvapi.security.JwtAuthFilter;
import br.ufjf.sgcvapi.security.JwtService;
import br.ufjf.sgcvapi.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.context.annotation.Configuration;

//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig /*extends WebSecurityConfigurerAdapter*/ {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    // ============================== MOVIDO ============================== //
    // Estava causando loop entre SecutiryConfig e UsuarioService
    // -------------------------------------------------------------------- //
    //    @Bean
    //    PasswordEncoder passwordEncoder() {
    //        return new BCryptPasswordEncoder();
    //    }
    // ===================================================================== //

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    // ============================== LEGADO ============================== //
    //    @Bean
    //    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        auth
    //            .userDetailsService(usuarioService)
    //            .passwordEncoder(passwordEncoder());
    //    }
    // ===================================================================== //

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/agendamentos/**")
                    .permitAll()
                .requestMatchers("/api/v1/clientes/**")
                    .permitAll()
                .requestMatchers("/api/v1/consultas/**")
                    .permitAll()
                .requestMatchers("/api/v1/consultaServicos/**")
                    .permitAll()
                .requestMatchers("/api/v1/consultaVeterinarios/**")
                    .permitAll()
                .requestMatchers("/api/v1/disponibilidades/**")
                    .permitAll()
                .requestMatchers("/api/v1/enderecos/**")
                    .permitAll()
                .requestMatchers("/api/v1/especializacoes/**")
                    .permitAll()
                .requestMatchers("/api/v1/especies/**")
                    .permitAll()
                .requestMatchers("/api/v1/medicamentoConsultas/**")
                    .permitAll()
                .requestMatchers("/api/v1/medicamentos/**")
                    .permitAll()
                .requestMatchers("/api/v1/medicamentoPets/**")
                    .permitAll()
                .requestMatchers("/api/v1/pets/**")
                    .permitAll()
                .requestMatchers("/api/v1/racas/**")
                    .permitAll()
                .requestMatchers("/api/v1/servicos/**")
                    .permitAll()
                .requestMatchers("/api/v1/servicoEspecializacaos/**")
                    .permitAll()
                .requestMatchers("/api/v1/vacinaConsultas/**")
                    .permitAll()
                .requestMatchers("/api/v1/vacinas/**")
                    .permitAll()
                .requestMatchers("/api/v1/veterinarios/**")
                    .hasAnyRole("ADMIN")
                .requestMatchers("/api/v1/veterinarioEspecializacoes/**")
                    .permitAll()
                .requestMatchers("/api/v1/usuarios/**")
                    .permitAll()

                // configure(WebSecurity)
                .requestMatchers("/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/**"
                )
                    .permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
        return http.build();
    }

    // ============================== LEGADO ============================== //
    //    @Override
    //    public void configure(WebSecurity web) throws Exception {
    //        web.ignoring().antMatchers(
    //                "/v2/api-docs",
    //                "/configuration/ui",
    //                "/swagger-resources/**",
    //                "/configuration/security",
    //                "/swagger-ui.html",
    //                "/webjars/**");
    //    }
    // ===================================================================== //
}