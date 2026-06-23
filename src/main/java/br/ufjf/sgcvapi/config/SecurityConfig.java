package br.ufjf.sgcvapi.config;

import br.ufjf.sgcvapi.security.JwtAuthFilter;
import br.ufjf.sgcvapi.security.JwtService;
import br.ufjf.sgcvapi.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(usuarioService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().disable()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/v1/agendamentos/**")
                    .permitAll()
                .antMatchers("/api/v1/clientes/**")
                    .permitAll()
                .antMatchers("/api/v1/consultas/**")
                    .permitAll()
                .antMatchers("/api/v1/consultaServicos/**")
                    .permitAll()
                .antMatchers("/api/v1/consultaVeterinarios/**")
                    .permitAll()
                .antMatchers("/api/v1/disponibilidades/**")
                    .permitAll()
                .antMatchers("/api/v1/enderecos/**")
                    .permitAll()
                .antMatchers("/api/v1/especializacoes/**")
                    .permitAll()
                .antMatchers("/api/v1/especies/**")
                    .permitAll()
                .antMatchers("/api/v1/medicamentoConsultas/**")
                    .permitAll()
                .antMatchers("/api/v1/medicamentos/**")
                    .permitAll()
                .antMatchers("/api/v1/medicamentoPets/**")
                    .permitAll()
                .antMatchers("/api/v1/pets/**")
                    .permitAll()
                .antMatchers("/api/v1/racas/**")
                    .permitAll()
                .antMatchers("/api/v1/servicos/**")
                    .permitAll()
                .antMatchers("/api/v1/servicoEspecializacaos/**")
                    .permitAll()
                .antMatchers("/api/v1/vacinaConsultas/**")
                    .permitAll()
                .antMatchers("/api/v1/vacinas/**")
                    .permitAll()
                .antMatchers("/api/v1/veterinarios/**")
                    .permitAll()
                .antMatchers("/api/v1/veterinarioEspecializacoes/**")
                    .permitAll()
//                .antMatchers("/api/v1/vagas/**")
//                    .hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
    }

    @Bean
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}