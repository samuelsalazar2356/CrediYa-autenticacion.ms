package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    boolean existePorCorreoElectronico(String correoElectronico);
}