package co.com.crediya.api;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestConfig.class)
class ApiRestTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    private RestTestClient client;

    @BeforeEach
    void setup() {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombres("Carlos")
                .apellidos("Perez")
                .correoElectronico("carlos@correo.com")
                .salarioBase(new BigDecimal("3500000"))
                .build();

        when(registrarUsuarioUseCase.registrar(any(Usuario.class))).thenReturn(usuario);

        client.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuario)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombres").isEqualTo("Carlos");
    }

    @Test
    void shouldReturnNotFoundForInvalidPath() {
        client.get()
                .uri("/api/v1/invalid")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
