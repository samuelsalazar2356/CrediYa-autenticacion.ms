package co.com.crediya.api;

import co.com.crediya.api.dto.UsuarioRequestDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        UsuarioRequestDTO requestDTO = UsuarioRequestDTO.builder()
                .nombres("Carlos")
                .apellidos("Perez")
                .fechaNacimiento(LocalDate.of(1995, 5, 15))
                .telefono("3001234567")
                .correoElectronico("carlos@correo.com")
                .salarioBase(new BigDecimal("3500000"))
                .build();

        Usuario usuarioCreado = Usuario.builder()
                .id(1L)
                .nombres("Carlos")
                .apellidos("Perez")
                .fechaNacimiento(LocalDate.of(1995, 5, 15))
                .telefono("3001234567")
                .correoElectronico("carlos@correo.com")
                .salarioBase(new BigDecimal("3500000"))
                .estado("ACTIVO")
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(registrarUsuarioUseCase.registrar(any(Usuario.class))).thenReturn(usuarioCreado);

        client.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.mensaje").isEqualTo("Usuario registrado exitosamente en el sistema CrediYa")
                .jsonPath("$.usuario.id").isEqualTo(1)
                .jsonPath("$.usuario.nombres").isEqualTo("Carlos")
                .jsonPath("$.usuario.estado").isEqualTo("ACTIVO");
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
