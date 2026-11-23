package co.com.crediya.api;

import co.com.crediya.api.dto.SolicitudItemRevisionDTO;
import co.com.crediya.api.dto.SolicitudListadoPaginadoDTO;
import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.dto.SolicitudResponseDTO;
import co.com.crediya.api.security.JwtAuthorizationFilter;
import co.com.crediya.model.comun.Pagina;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudRevisionDetalle;
import co.com.crediya.model.usuario.TokenClaims;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.usecase.consultarsolicitudes.ConsultarSolicitudesRevisionUseCase;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de presentacion REST para la radicacion y consulta de solicitudes de credito.
 * Expone POST /api/v1/solicitud y GET /api/v1/solicitud.
 */
@RestController
@RequestMapping(value = "/api/v1/solicitud", produces = MediaType.APPLICATION_JSON_VALUE)
public class SolicitudApiRest {

    private static final Logger log = LoggerFactory.getLogger(SolicitudApiRest.class);

    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final ConsultarSolicitudesRevisionUseCase consultarSolicitudesRevisionUseCase;

    public SolicitudApiRest(RegistrarSolicitudUseCase registrarSolicitudUseCase,
                            ConsultarSolicitudesRevisionUseCase consultarSolicitudesRevisionUseCase) {
        this.registrarSolicitudUseCase = registrarSolicitudUseCase;
        this.consultarSolicitudesRevisionUseCase = consultarSolicitudesRevisionUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolicitudResponseDTO> radicarSolicitud(@RequestBody SolicitudRequestDTO requestDTO,
                                                                HttpServletRequest httpRequest) {
        log.info("Iniciando procesamiento de radicacion de solicitud de prestamo");

        // Validacion BOLA/IDOR: El cliente autenticado solo puede solicitar a su propio nombre
        TokenClaims claims = (TokenClaims) httpRequest.getAttribute(JwtAuthorizationFilter.CLAIMS_ATTRIBUTE);
        if (claims != null && claims.getCedula() != null &&
                !claims.getCedula().equalsIgnoreCase(requestDTO.getDocumentoIdentidad())) {
            throw new BusinessException("Un cliente solo puede radicar solicitudes de credito a su propio nombre y documento");
        }

        Solicitud solicitud = requestDTO.toDomain();
        Solicitud solicitudCreada = registrarSolicitudUseCase.registrar(solicitud);

        log.info("Solicitud de prestamo radicada exitosamente con ID: {} bajo estado: {}",
                solicitudCreada.getId(), solicitudCreada.getEstado());

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudResponseDTO.exitoso(solicitudCreada));
    }

    @GetMapping
    public ResponseEntity<SolicitudListadoPaginadoDTO> listarSolicitudesParaRevision(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "estado", required = false) String estado) {

        log.info("Consultando listado de solicitudes para revision manual. Pagina: {}, Tamano: {}, Estado: {}",
                page, size, estado);

        Pagina<SolicitudRevisionDetalle> paginaResultado = consultarSolicitudesRevisionUseCase.consultar(page, size, estado);

        log.info("Consulta de revision completada. Total registros encontrados: {}", paginaResultado.getTotalElementos());

        return ResponseEntity.status(HttpStatus.OK).body(SolicitudListadoPaginadoDTO.desdeDominio(paginaResultado));
    }
}
