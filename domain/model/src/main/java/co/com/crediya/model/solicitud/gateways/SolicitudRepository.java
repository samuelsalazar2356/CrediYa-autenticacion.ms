package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Solicitud;

import java.util.Optional;

/**
 * Puerto de salida para la persistencia del agregado Solicitud.
 * Permite desacoplar el caso de uso del motor de base de datos relacional.
 */
public interface SolicitudRepository {

    Solicitud guardar(Solicitud solicitud);

    Optional<Solicitud> buscarPorId(Long id);
}
