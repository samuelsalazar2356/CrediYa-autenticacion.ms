package co.com.crediya.api;

import co.com.crediya.api.config.CorsConfig;
import co.com.crediya.api.security.InMemoryTokenRevocationAdapter;
import co.com.crediya.api.security.JwtAuthorizationFilter;
import co.com.crediya.api.security.JwtTokenProviderAdapter;
import co.com.crediya.api.security.LoginRateLimiterFilter;
import co.com.crediya.api.security.PBKDF2PasswordEncoderAdapter;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.TokenProviderGateway;
import co.com.crediya.model.usuario.gateways.TokenRevocationGateway;
import co.com.crediya.usecase.autenticacion.AutenticarUsuarioUseCase;
import co.com.crediya.usecase.consultarsolicitudes.ConsultarSolicitudesRevisionUseCase;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
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
                classes = {CorsConfig.class, JwtAuthorizationFilter.class, LoginRateLimiterFilter.class}
        ))
@Import({ApiRest.class, SolicitudApiRest.class, LoginApiRest.class, GlobalExceptionHandler.class})
public class TestConfig {

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase() {
        return Mockito.mock(RegistrarUsuarioUseCase.class);
    }

    @Bean
    public RegistrarSolicitudUseCase registrarSolicitudUseCase() {
        return Mockito.mock(RegistrarSolicitudUseCase.class);
    }

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase() {
        return Mockito.mock(AutenticarUsuarioUseCase.class);
    }

    @Bean
    public ConsultarSolicitudesRevisionUseCase consultarSolicitudesRevisionUseCase() {
        return Mockito.mock(ConsultarSolicitudesRevisionUseCase.class);
    }

    @Bean
    public PasswordEncoderGateway passwordEncoderGateway() {
        return new PBKDF2PasswordEncoderAdapter();
    }

    @Bean
    public TokenProviderGateway tokenProviderGateway() {
        return new JwtTokenProviderAdapter(
                "CrediYaSuperSecretKeyForJwtAuthentication2026ProductionStandardMustBe256BitsLong!",
                60
        );
    }
}
