package co.com.crediya.jpa.solicitud;

import co.com.crediya.jpa.helper.AdapterOperations;
import co.com.crediya.model.comun.Pagina;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(readOnly = true)
    public Pagina<Solicitud> buscarPorEstadosPaginado(List<String> estados, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SolicitudData> resultadoPage = repository.findByEstadoIn(estados, pageable);
        List<Solicitud> contenido = resultadoPage.getContent().stream()
                .map(this::toEntity)
                .toList();

        return new Pagina<>(contenido, resultadoPage.getNumber(), resultadoPage.getSize(), resultadoPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> buscarAprobadasPorDocumento(String documentoIdentidad) {
        return repository.findAprobadasByDocumentoIdentidad(documentoIdentidad).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<Solicitud>> buscarAprobadasPorDocumentos(Set<String> documentosIdentidad) {
        if (documentosIdentidad == null || documentosIdentidad.isEmpty()) {
            return Map.of();
        }
        return repository.findAprobadasByDocumentosIdentidad(documentosIdentidad).stream()
                .map(this::toEntity)
                .collect(Collectors.groupingBy(Solicitud::getDocumentoIdentidad));
    }
}
