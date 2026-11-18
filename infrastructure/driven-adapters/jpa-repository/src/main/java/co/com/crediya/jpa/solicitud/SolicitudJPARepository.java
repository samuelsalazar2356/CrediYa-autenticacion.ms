package co.com.crediya.jpa.solicitud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface SolicitudJPARepository extends CrudRepository<SolicitudData, Long>, QueryByExampleExecutor<SolicitudData> {
}
