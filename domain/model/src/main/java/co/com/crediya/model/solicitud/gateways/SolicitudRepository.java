package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.comun.Pagina;
import co.com.crediya.model.solicitud.Solicitud;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Puerto de salida para la persistencia del agregado Solicitud.
 * Permite desacoplar el caso de uso del motor de base de datos relacional.
 */
public interface SolicitudRepository {

    Solicitud guardar(Solicitud solicitud);

    Optional<Solicitud> buscarPorId(Long id);

    Pagina<Solicitud> buscarPorEstadosPaginado(List<String> estados, int page, int size);

    List<Solicitud> buscarAprobadasPorDocumento(String documentoIdentidad);

    Map<String, List<Solicitud>> buscarAprobadasPorDocumentos(Set<String> documentosIdentidad);
}
