package co.com.crediya.jpa.solicitud;

import co.com.crediya.jpa.helper.AdapterOperations;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador de infraestructura para la persistencia del agregado Solicitud en PostgreSQL.
 * Aplica transaccionalidad ACID en operaciones de escritura.
 */
@Repository
public class SolicitudJPARepositoryAdapter extends AdapterOperations<Solicitud, SolicitudData, Long, SolicitudJPARepository>
        implements SolicitudRepository {

    public SolicitudJPARepositoryAdapter(SolicitudJPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }

    @Override
    @Transactional
    public Solicitud guardar(Solicitud solicitud) {
        return super.save(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Solicitud> buscarPorId(Long id) {
        return Optional.ofNullable(super.findById(id));
    }
}
