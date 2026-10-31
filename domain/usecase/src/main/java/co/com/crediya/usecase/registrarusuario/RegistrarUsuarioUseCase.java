package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Usuario registrar(Usuario usuario) {
        validarCamposObligatorios(usuario);
        validarFormatoCorreo(usuario.getCorreoElectronico());
        validarRangoSalario(usuario.getSalarioBase());
        validarCorreoUnico(usuario.getCorreoElectronico());

        return usuarioRepository.guardar(usuario);
    }

    private void validarCamposObligatorios(Usuario usuario) {
        if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
            throw new BusinessException("El campo 'nombres' es obligatorio y no puede estar vacío");
        }
        if (usuario.getApellidos() == null || usuario.getApellidos().trim().isEmpty()) {
            throw new BusinessException("El campo 'apellidos' es obligatorio y no puede estar vacío");
        }
        if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().trim().isEmpty()) {
            throw new BusinessException("El campo 'correo_electronico' es obligatorio y no puede estar vacío");
        }
        if (usuario.getSalarioBase() == null) {
            throw new BusinessException("El campo 'salario_base' es obligatorio");
        }
    }

    private void validarFormatoCorreo(String correo) {
        if (!EMAIL_PATTERN.matcher(correo.trim()).matches()) {
            throw new BusinessException("El correo electrónico no tiene un formato válido");
        }
    }

    private void validarRangoSalario(BigDecimal salario) {
        if (salario.compareTo(SALARIO_MINIMO) < 0 || salario.compareTo(SALARIO_MAXIMO) > 0) {
            throw new BusinessException("El salario base debe estar entre 0 y 15,000,000");
        }
    }

    private void validarCorreoUnico(String correo) {
        if (usuarioRepository.existePorCorreoElectronico(correo.trim())) {
            throw new BusinessException("El correo electrónico ya se encuentra registrado por otro solicitante");
        }
    }
}