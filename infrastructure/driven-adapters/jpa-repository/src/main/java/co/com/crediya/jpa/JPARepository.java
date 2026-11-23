package co.com.crediya.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

public interface JPARepository extends CrudRepository<UsuarioData, Long>, QueryByExampleExecutor<UsuarioData> {
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByCedula(String cedula);
    Optional<UsuarioData> findByCorreoElectronico(String correoElectronico);
    Optional<UsuarioData> findByCedula(String cedula);
}
