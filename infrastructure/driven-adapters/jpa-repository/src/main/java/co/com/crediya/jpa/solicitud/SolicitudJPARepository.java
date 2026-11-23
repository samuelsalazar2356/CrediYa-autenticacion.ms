package co.com.crediya.jpa.solicitud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SolicitudJPARepository extends JpaRepository<SolicitudData, Long> {

    @Query("SELECT s FROM SolicitudData s WHERE s.estado IN :estados ORDER BY s.id DESC")
    Page<SolicitudData> findByEstadoIn(@Param("estados") Collection<String> estados, Pageable pageable);

    @Query("SELECT s FROM SolicitudData s WHERE s.documentoIdentidad = :documento AND LOWER(s.estado) LIKE '%aprobad%'")
    List<SolicitudData> findAprobadasByDocumentoIdentidad(@Param("documento") String documentoIdentidad);
}
