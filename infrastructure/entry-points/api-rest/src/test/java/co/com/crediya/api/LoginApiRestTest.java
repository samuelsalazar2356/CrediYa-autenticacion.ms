package co.com.crediya.api;

import co.com.crediya.api.dto.LoginRequestDTO;
import co.com.crediya.model.usuario.RolUsuario;
import co.com.crediya.model.usuario.SesionUsuario;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.usecase.autenticacion.AutenticarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestConfig.class)
class LoginApiRestTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    private RestTestClient client;

    @BeforeEach
    void setup() {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void shouldLoginSuccessfully() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .cedula("1098765432")
                .nombres("Carlos")
                .apellidos("Perez")
                .correoElectronico("carlos@crediya.com")
                .rol(RolUsuario.CLIENTE)
                .build();

        SesionUsuario sesion = new SesionUsuario("token.jwt.valido", "Bearer", usuario);

        when(autenticarUsuarioUseCase.autenticar(eq("carlos@crediya.com"), eq("Clave123*")))
                .thenReturn(sesion);

        LoginRequestDTO request = new LoginRequestDTO("carlos@crediya.com", "Clave123*");

        client.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isEqualTo("token.jwt.valido")
                .jsonPath("$.tipoToken").isEqualTo("Bearer")
                .jsonPath("$.correoElectronico").isEqualTo("carlos@crediya.com")
                .jsonPath("$.rol").isEqualTo("CLIENTE");
    }

    @Test
    void shouldFailWhenCredentialsAreInvalid() {
        when(autenticarUsuarioUseCase.autenticar(eq("invalido@crediya.com"), eq("malaClave")))
                .thenThrow(new BusinessException("Credenciales de autenticacion invalidas"));

        LoginRequestDTO request = new LoginRequestDTO("invalido@crediya.com", "malaClave");

        client.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Error de Validación de Negocio")
                .jsonPath("$.mensaje").isEqualTo("Credenciales de autenticacion invalidas");
    }

    @Test
    void shouldLogoutAndRevokeTokenSuccessfully() {
        client.post()
                .uri("/api/v1/login/logout")
                .header("Authorization", "Bearer token.para.revocar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.mensaje").isEqualTo("Sesion cerrada exitosamente y token revocado");
    }
}
