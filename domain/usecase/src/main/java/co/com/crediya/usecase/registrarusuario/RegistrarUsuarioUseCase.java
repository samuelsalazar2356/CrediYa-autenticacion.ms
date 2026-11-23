package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.RolUsuario;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.UsuarioValidador;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.PasswordEncoderGateway;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;

import java.time.LocalDateTime;

public class RegistrarUsuarioUseCase {

    private static final String ESTADO_INICIAL = "ACTIVO";
    private static final String CONTRASENA_DEFECTO = "CrediYa2026*";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public RegistrarUsuarioUseCase(UsuarioRepository usuarioRepository, PasswordEncoderGateway passwordEncoderGateway) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public RegistrarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this(usuarioRepository, null);
    }

    public Usuario registrar(Usuario usuario) {
        // 1. Validaciones intrinsecas del modelo (formatos, seguridad y normalizacion)
        Usuario usuarioNormalizado = UsuarioValidador.validarYNormalizar(usuario);

        // 2. Reglas de negocio que requieren consulta al Gateway (Base de Datos)
        validarUnicidadEnSistema(usuarioNormalizado);

        // 3. Resolver rol por defecto si no fue provisto
        RolUsuario rolFinal = usuarioNormalizado.getRol() != null ? usuarioNormalizado.getRol() : RolUsuario.CLIENTE;

        // 4. Resolver y hashear contrasena
        String contrasenaPlana = usuarioNormalizado.getContrasenaHash() != null && !usuarioNormalizado.getContrasenaHash().trim().isEmpty()
                ? usuarioNormalizado.getContrasenaHash()
                : CONTRASENA_DEFECTO;

        String contrasenaHasheada = passwordEncoderGateway != null
                ? passwordEncoderGateway.codificar(contrasenaPlana)
                : contrasenaPlana;

        // 5. Establecer estado inicial, auditoria y credenciales
        Usuario usuarioParaGuardar = usuarioNormalizado.toBuilder()
                .estado(ESTADO_INICIAL)
                .fechaCreacion(LocalDateTime.now())
                .rol(rolFinal)
                .contrasenaHash(contrasenaHasheada)
                .build();

        // 6. Persistir a traves del puerto
        return usuarioRepository.guardar(usuarioParaGuardar);
    }

    private void validarUnicidadEnSistema(Usuario usuario) {
        if (usuarioRepository.existePorCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new BusinessException("El correo electronico ya se encuentra registrado por otro solicitante");
        }
        if (usuarioRepository.existePorCedula(usuario.getCedula())) {
            throw new BusinessException("La cedula ingresada ya se encuentra registrada en el sistema");
        }
    }
}
