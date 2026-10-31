package co.com.crediya.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JPARepository extends CrudRepository<UsuarioData, Long>, QueryByExampleExecutor<UsuarioData> {
    boolean existsByCorreoElectronico(String correoElectronico);
}