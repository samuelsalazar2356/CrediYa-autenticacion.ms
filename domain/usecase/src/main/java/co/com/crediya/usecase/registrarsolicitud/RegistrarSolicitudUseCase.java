package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.SolicitudValidador;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;

import java.time.LocalDateTime;

/**
 * Caso de Uso: Registrar una solicitud de prestamo.
 * Responsabilidad: Orquestar la validacion de negocio, asignar estado inicial y persistir.
 */
public class RegistrarSolicitudUseCase {

    private static final String ESTADO_INICIAL = "Pendiente de revisión";

    private final SolicitudRepository solicitudRepository;

    public RegistrarSolicitudUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Registra una nueva solicitud de credito en el sistema.
     *
     * @param solicitud Entidad con los datos suministrados por el solicitante.
     * @return Solicitud registrada con identificador asignado y estado inicial.
     */
    public Solicitud registrar(Solicitud solicitud) {
        Solicitud solicitudNormalizada = SolicitudValidador.validarYNormalizar(solicitud);

        Solicitud solicitudListaParaPersistir = solicitudNormalizada.toBuilder()
                .estado(ESTADO_INICIAL)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return solicitudRepository.guardar(solicitudListaParaPersistir);
    }
}
