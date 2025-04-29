package br.gov.ce.direitoshumanos.api_agenda.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // liberar tudo para testes
                        .allowedOrigins(
                                "http://localhost:3000"
                        ) // seu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization") // 👈 importante se quiser que o frontend leia o header Authorization de volta
                        .allowCredentials(true); // importante se usar cookies ou autenticação baseada em sessão
            }
        };
    }
}
