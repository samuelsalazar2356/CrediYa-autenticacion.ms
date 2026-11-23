package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    boolean existePorCorreoElectronico(String correoElectronico);
    boolean existePorCedula(String cedula);
    Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico);
    Optional<Usuario> buscarPorCedula(String cedula);
}
