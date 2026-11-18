package co.com.crediya.api;

import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestConfig.class)
class SolicitudApiRestTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RegistrarSolicitudUseCase registrarSolicitudUseCase;

    private RestTestClient client;

    @BeforeEach
    void setup() {
        client = RestTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void shouldRegisterSolicitudSuccessfully() {
        SolicitudRequestDTO requestDTO = SolicitudRequestDTO.builder()
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .tipoPrestamo("PERSONAL")
                .build();

        Solicitud solicitudCreada = Solicitud.builder()
                .id(101L)
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("5000000.00"))
                .plazoMeses(24)
                .tipoPrestamo(TipoPrestamo.PERSONAL)
                .estado("Pendiente de revisión")
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(registrarSolicitudUseCase.registrar(any(Solicitud.class))).thenReturn(solicitudCreada);

        client.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.mensaje").isEqualTo("Solicitud de prestamo radicada exitosamente")
                .jsonPath("$.idSolicitud").isEqualTo(101)
                .jsonPath("$.estado").isEqualTo("Pendiente de revisión")
                .jsonPath("$.tipoPrestamo").isEqualTo("Personal");
    }

    @Test
    void shouldFailWhenTipoPrestamoIsInvalid() {
        SolicitudRequestDTO requestDTO = SolicitudRequestDTO.builder()
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .tipoPrestamo("TIPO_INEXISTENTE")
                .build();

        client.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Error de Validación de Negocio");
    }
}
