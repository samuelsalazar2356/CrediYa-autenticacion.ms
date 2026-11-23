package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    boolean existePorCorreoElectronico(String correoElectronico);
    boolean existePorCedula(String cedula);
    Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico);
    Optional<Usuario> buscarPorCedula(String cedula);
    Map<String, Usuario> buscarPorCedulas(Set<String> cedulas);
}
