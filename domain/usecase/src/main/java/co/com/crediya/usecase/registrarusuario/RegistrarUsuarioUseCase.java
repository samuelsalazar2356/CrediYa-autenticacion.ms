package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.UsuarioValidador;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;

import java.time.LocalDateTime;


public class RegistrarUsuarioUseCase {

    private static final String ESTADO_INICIAL = "ACTIVO";

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        // 1. Validaciones intrínsecas del modelo (formatos, seguridad y normalización)
        Usuario usuarioNormalizado = UsuarioValidador.validarYNormalizar(usuario);

        // 2. Reglas de negocio que requieren consulta al Gateway (Base de Datos)
        validarUnicidadEnSistema(usuarioNormalizado);

        // 3. Establecer estado inicial y fecha de auditoría del servidor
        Usuario usuarioParaGuardar = usuarioNormalizado.toBuilder()
                .estado(ESTADO_INICIAL)
                .fechaCreacion(LocalDateTime.now())
                .build();

        // 4. Persistir a través del puerto
        return usuarioRepository.guardar(usuarioParaGuardar);
    }

    private void validarUnicidadEnSistema(Usuario usuario) {
        if (usuarioRepository.existePorCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new BusinessException("El correo electrónico ya se encuentra registrado por otro solicitante");
        }
        if (usuarioRepository.existePorCedula(usuario.getCedula())) {
            throw new BusinessException("La cédula ingresada ya se encuentra registrada en el sistema");
        }
    }
}
