package br.ufjf.sgcvapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SGCV API")
                        .description("Sistema de Gestão de Clínica Veterinária")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Vanessa P K Trajano & Eduarda P M Nunes")
                                .url("https://github.com/VanessaTrajano/ClinicaVeterinariaBackEnd")
                                .email("vanessa.trajano@estudante.ufjf.br, eduarda.nunes@estudante.ufjf.br")
                        )
                )

                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .in(SecurityScheme.In.HEADER)
                                .bearerFormat("JWT")
                        )
                );
    }
}