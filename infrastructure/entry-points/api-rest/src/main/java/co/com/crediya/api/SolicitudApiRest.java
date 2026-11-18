package co.com.crediya.api;

import co.com.crediya.api.dto.SolicitudRequestDTO;
import co.com.crediya.api.dto.SolicitudResponseDTO;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de presentacion REST para la radicacion de solicitudes de credito.
 * Expone el endpoint POST /api/v1/solicitud.
 */
@RestController
@RequestMapping(value = "/api/v1/solicitud", produces = MediaType.APPLICATION_JSON_VALUE)
public class SolicitudApiRest {

    private static final Logger log = LoggerFactory.getLogger(SolicitudApiRest.class);
    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;

    public SolicitudApiRest(RegistrarSolicitudUseCase registrarSolicitudUseCase) {
        this.registrarSolicitudUseCase = registrarSolicitudUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolicitudResponseDTO> radicarSolicitud(@RequestBody SolicitudRequestDTO requestDTO) {
        log.info("Iniciando procesamiento de radicacion de solicitud de prestamo");

        Solicitud solicitud = requestDTO.toDomain();
        Solicitud solicitudCreada = registrarSolicitudUseCase.registrar(solicitud);

        log.info("Solicitud de prestamo radicada exitosamente con ID: {} bajo estado: {}",
                solicitudCreada.getId(), solicitudCreada.getEstado());

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudResponseDTO.exitoso(solicitudCreada));
    }
}
