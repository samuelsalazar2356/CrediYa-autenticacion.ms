package co.com.crediya.usecase.consultarsolicitudes;

import co.com.crediya.model.comun.Pagina;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudRevisionDetalle;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarSolicitudesRevisionUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private ConsultarSolicitudesRevisionUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConsultarSolicitudesRevisionUseCase(solicitudRepository, usuarioRepository);
    }

    @Test
    void shouldReturnPaginatedSolicitudesWithAggregatedData() {
        Solicitud solicitud = Solicitud.builder()
                .id(10L)
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("5000000"))
                .plazoMeses(24)
                .tipoPrestamo(TipoPrestamo.PERSONAL)
                .estado("Pendiente de revisión")
                .build();

        Pagina<Solicitud> paginaSolicitudes = new Pagina<>(List.of(solicitud), 0, 10, 1);

        Usuario usuario = Usuario.builder()
                .cedula("1098765432")
                .nombres("Carlos")
                .apellidos("Perez")
                .correoElectronico("carlos@crediya.com")
                .salarioBase(new BigDecimal("4000000"))
                .build();

        Solicitud solicitudAprobadaPrevia = Solicitud.builder()
                .id(5L)
                .documentoIdentidad("1098765432")
                .monto(new BigDecimal("2000000"))
                .plazoMeses(12)
                .tipoPrestamo(TipoPrestamo.PERSONAL)
                .estado("Aprobado")
                .build();

        when(solicitudRepository.buscarPorEstadosPaginado(anyList(), anyInt(), anyInt()))
                .thenReturn(paginaSolicitudes);
        when(usuarioRepository.buscarPorCedula("1098765432"))
                .thenReturn(Optional.of(usuario));
        when(solicitudRepository.buscarAprobadasPorDocumento("1098765432"))
                .thenReturn(List.of(solicitudAprobadaPrevia));

        Pagina<SolicitudRevisionDetalle> resultado = useCase.consultar(0, 10, null);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContenido().size());

        SolicitudRevisionDetalle detalle = resultado.getContenido().get(0);
        assertEquals(10L, detalle.getIdSolicitud());
        assertEquals(new BigDecimal("5000000"), detalle.getMonto());
        assertEquals(24, detalle.getPlazo());
        assertEquals("carlos@crediya.com", detalle.getEmail());
        assertEquals("Carlos Perez", detalle.getNombre());
        assertEquals("Personal", detalle.getTipoPrestamo());
        assertEquals(new BigDecimal("0.0150"), detalle.getTasaInteres());
        assertEquals("Pendiente de revisión", detalle.getEstadoSolicitud());
        assertEquals(new BigDecimal("4000000"), detalle.getSalarioBase());
        assertNotNull(detalle.getDeudaTotalMensualSolicitudesAprobadas());
        assertFalse(detalle.getDeudaTotalMensualSolicitudesAprobadas().compareTo(BigDecimal.ZERO) == 0);
    }
}
