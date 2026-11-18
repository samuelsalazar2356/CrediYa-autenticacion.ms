package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarSolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    private RegistrarSolicitudUseCase registrarSolicitudUseCase;

    @BeforeEach
    void setup() {
        registrarSolicitudUseCase = new RegistrarSolicitudUseCase(solicitudRepository);
    }

    @Test
    void shouldRegisterSolicitudWithPendingStatus() {
        Solicitud solicitudInput = Solicitud.builder()
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("10000000"))
                .plazoMeses(36)
                .tipoPrestamo(TipoPrestamo.LIBRE_INVERSION)
                .build();

        when(solicitudRepository.guardar(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud argumento = invocation.getArgument(0);
            return argumento.toBuilder().id(1L).build();
        });

        Solicitud resultado = registrarSolicitudUseCase.registrar(solicitudInput);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pendiente de revisión", resultado.getEstado());
        assertNotNull(resultado.getFechaCreacion());
        verify(solicitudRepository).guardar(any(Solicitud.class));
    }

    @Test
    void shouldThrowExceptionWhenMontoIsBelowMinimum() {
        Solicitud solicitudInvalida = Solicitud.builder()
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("50000"))
                .plazoMeses(12)
                .tipoPrestamo(TipoPrestamo.PERSONAL)
                .build();

        BusinessException excepcion = assertThrows(BusinessException.class, () ->
                registrarSolicitudUseCase.registrar(solicitudInvalida));

        assertEquals("El monto minimo de solicitud es $100.000,00 COP", excepcion.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPlazoExceedsMax() {
        Solicitud solicitudInvalida = Solicitud.builder()
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("1000000"))
                .plazoMeses(140)
                .tipoPrestamo(TipoPrestamo.VIVIENDA)
                .build();

        BusinessException excepcion = assertThrows(BusinessException.class, () ->
                registrarSolicitudUseCase.registrar(solicitudInvalida));

        assertEquals("El plazo debe estar comprendido entre 1 y 120 meses", excepcion.getMessage());
    }
}
