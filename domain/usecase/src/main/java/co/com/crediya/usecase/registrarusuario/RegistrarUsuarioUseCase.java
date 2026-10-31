package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\d{10}$");
    private static final int EDAD_MINIMA = 18;
    private static final String ESTADO_INICIAL = "ACTIVO";

    public Usuario registrar(Usuario usuario) {
        validarCamposObligatorios(usuario);
        validarFormatoTelefono(usuario.getTelefono());
        validarMayorDeEdad(usuario.getFechaNacimiento());

        // Sanitización y normalización (Trim y minúsculas en correo)
        String nombresNormalizados = usuario.getNombres().trim();
        String apellidosNormalizados = usuario.getApellidos().trim();
        String correoNormalizado = usuario.getCorreoElectronico().trim().toLowerCase();
        String telefonoNormalizado = usuario.getTelefono().trim();
        String direccionNormalizada = usuario.getDireccion() != null ? usuario.getDireccion().trim() : null;

        validarFormatoCorreo(correoNormalizado);
        validarRangoSalario(usuario.getSalarioBase());
        validarCorreoUnico(correoNormalizado);

        Usuario usuarioAguardar = usuario.toBuilder()
                .nombres(nombresNormalizados)
                .apellidos(apellidosNormalizados)
                .correoElectronico(correoNormalizado)
                .telefono(telefonoNormalizado)
                .direccion(direccionNormalizada)
                .estado(ESTADO_INICIAL)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return usuarioRepository.guardar(usuarioAguardar);
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
        if (usuario.getFechaNacimiento() == null) {
            throw new BusinessException("El campo 'fecha_nacimiento' es obligatorio");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
            throw new BusinessException("El campo 'telefono' es obligatorio y no puede estar vacío");
        }
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de nacimiento no puede ser en el futuro");
        }
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < EDAD_MINIMA) {
            throw new BusinessException("El solicitante debe ser mayor de edad (mínimo 18 años)");
        }
    }

    private void validarFormatoTelefono(String telefono) {
        if (!TELEFONO_PATTERN.matcher(telefono.trim()).matches()) {
            throw new BusinessException("El teléfono debe contener exactamente 10 dígitos numéricos");
        }
    }

    private void validarFormatoCorreo(String correo) {
        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            throw new BusinessException("El correo electrónico no tiene un formato válido");
        }
    }

    private void validarRangoSalario(BigDecimal salario) {
        if (salario.compareTo(SALARIO_MINIMO) < 0 || salario.compareTo(SALARIO_MAXIMO) > 0) {
            throw new BusinessException("El salario base debe estar entre 0 y 15,000,000");
        }
    }

    private void validarCorreoUnico(String correo) {
        if (usuarioRepository.existePorCorreoElectronico(correo)) {
            throw new BusinessException("El correo electrónico ya se encuentra registrado por otro solicitante");
        }
    }
}
