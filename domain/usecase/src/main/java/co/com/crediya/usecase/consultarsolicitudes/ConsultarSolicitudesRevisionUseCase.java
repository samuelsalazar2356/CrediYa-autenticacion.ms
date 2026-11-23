package co.com.crediya.usecase.consultarsolicitudes;

import co.com.crediya.model.comun.Pagina;
import co.com.crediya.model.solicitud.CalculadoraFinanciera;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudRevisionDetalle;
import co.com.crediya.model.solicitud.TipoPrestamo;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para la consulta paginada y enriquecida de solicitudes para revision manual del Asesor.
 */
public class ConsultarSolicitudesRevisionUseCase {

    private static final List<String> ESTADOS_REVISION_DEFAULT = List.of(
            "Pendiente de revisión",
            "Pendiente de revision",
            "Rechazadas",
            "Rechazada",
            "Revision manual",
            "Revisión manual"
    );

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public ConsultarSolicitudesRevisionUseCase(SolicitudRepository solicitudRepository, UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Pagina<SolicitudRevisionDetalle> consultar(int page, int size, String estadoFiltro) {
        int paginaValida = Math.max(0, page);
        int tamanoValido = (size <= 0 || size > 100) ? 10 : size;

        List<String> estadosConsulta;
        if (estadoFiltro != null && !estadoFiltro.trim().isEmpty()) {
            estadosConsulta = List.of(estadoFiltro.trim());
        } else {
            estadosConsulta = ESTADOS_REVISION_DEFAULT;
        }

        Pagina<Solicitud> paginaSolicitudes = solicitudRepository.buscarPorEstadosPaginado(estadosConsulta, paginaValida, tamanoValido);

        List<SolicitudRevisionDetalle> detalles = new ArrayList<>();
        for (Solicitud solicitud : paginaSolicitudes.getContenido()) {
            detalles.add(enriquecerSolicitud(solicitud));
        }

        return new Pagina<>(detalles, paginaSolicitudes.getNumeroPagina(),
                paginaSolicitudes.getTamanoPagina(), paginaSolicitudes.getTotalElementos());
    }

    private SolicitudRevisionDetalle enriquecerSolicitud(Solicitud solicitud) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorCedula(solicitud.getDocumentoIdentidad());

        String email = usuarioOpt.map(Usuario::getCorreoElectronico).orElse("Sin correo registrado");
        String nombre = usuarioOpt.map(u -> (u.getNombres() + " " + u.getApellidos()).trim()).orElse("Sin nombre registrado");
        BigDecimal salarioBase = usuarioOpt.map(Usuario::getSalarioBase).orElse(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

        TipoPrestamo tipo = solicitud.getTipoPrestamo();
        BigDecimal tasaInteres = tipo != null ? tipo.getTasaInteresMensual() : BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        String tipoDescripcion = tipo != null ? tipo.getDescripcion() : "Desconocido";

        BigDecimal deudaTotalMensual = calcularDeudaTotalMensualAprobada(solicitud.getDocumentoIdentidad());

        return new SolicitudRevisionDetalle(
                solicitud.getId(),
                solicitud.getMonto(),
                solicitud.getPlazoMeses(),
                email,
                nombre,
                tipoDescripcion,
                tasaInteres,
                solicitud.getEstado(),
                salarioBase,
                deudaTotalMensual
        );
    }

    private BigDecimal calcularDeudaTotalMensualAprobada(String documentoIdentidad) {
        List<Solicitud> aprobadas = solicitudRepository.buscarAprobadasPorDocumento(documentoIdentidad);
        BigDecimal totalDeuda = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (Solicitud s : aprobadas) {
            BigDecimal tasa = s.getTipoPrestamo() != null ? s.getTipoPrestamo().getTasaInteresMensual() : BigDecimal.ZERO;
            int plazo = s.getPlazoMeses() != null ? s.getPlazoMeses() : 0;
            BigDecimal cuota = CalculadoraFinanciera.calcularCuotaMensual(s.getMonto(), tasa, plazo);
            totalDeuda = totalDeuda.add(cuota);
        }

        return totalDeuda;
    }
}
