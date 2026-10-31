package co.com.crediya.api;

import co.com.crediya.api.config.CorsConfig;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan(basePackages = "co.com.crediya.api",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = CorsConfig.class
        ))
@Import({ApiRest.class, GlobalExceptionHandler.class})
public class TestConfig {

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase() {
        return Mockito.mock(RegistrarUsuarioUseCase.class);
    }
}