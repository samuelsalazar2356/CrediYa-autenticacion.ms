package co.com.crediya.usecase.autenticacion;

import co.com.crediya.model.usuario.SesionUsuario;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.TokenProviderGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;

/**
 * Caso de uso responsable de autenticar credenciales y expedir tokens de sesion.
 */
public class AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final TokenProviderGateway tokenProviderGateway;

    public AutenticarUsuarioUseCase(UsuarioRepository usuarioRepository,
                                    PasswordEncoderGateway passwordEncoderGateway,
                                    TokenProviderGateway tokenProviderGateway) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoderGateway = passwordEncoderGateway;
        this.tokenProviderGateway = tokenProviderGateway;
    }

    public SesionUsuario autenticar(String correoElectronico, String contrasenaPlana) {
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new BusinessException("El correo electronico es obligatorio para iniciar sesion");
        }
        if (contrasenaPlana == null || contrasenaPlana.trim().isEmpty()) {
            throw new BusinessException("La contrasena es obligatoria para iniciar sesion");
        }

        String correoNormalizado = correoElectronico.trim().toLowerCase();
        Usuario usuario = usuarioRepository.buscarPorCorreoElectronico(correoNormalizado)
                .orElseThrow(() -> new BusinessException("Credenciales de autenticacion invalidas"));

        if (usuario.getContrasenaHash() == null ||
                !passwordEncoderGateway.verificar(contrasenaPlana, usuario.getContrasenaHash())) {
            throw new BusinessException("Credenciales de autenticacion invalidas");
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new BusinessException("El usuario se encuentra inactivo en el sistema");
        }

        String token = tokenProviderGateway.generarToken(usuario);
        return new SesionUsuario(token, "Bearer", usuario);
    }
}
