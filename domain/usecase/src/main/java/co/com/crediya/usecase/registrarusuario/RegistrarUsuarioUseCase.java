package co.com.crediya.usecase.registrarusuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.excepcion.BusinessException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    // --- Constantes de negocio ---
    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final int EDAD_MINIMA = 18;
    private static final int EDAD_MAXIMA = 120;
    private static final String ESTADO_INICIAL = "ACTIVO";

    // --- Patrones de validación ---
    // Correo con dominio obligatorio (ej. usuario@correo.com)
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    // Solo 10 dígitos numéricos exactos
    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^\\d{10}$");
    // Solo letras (incluye tildes y n con tilde), espacios, guion y apostrofe
    private static final Pattern NOMBRE_PATTERN =
            Pattern.compile("^[a-zA-Z\u00e1\u00e9\u00ed\u00f3\u00fa\u00c1\u00c9\u00cd\u00d3\u00da\u00f1\u00d1\u00fc\u00dc\\s'\\-]+$");
    // Cédula: solo números, entre 6 y 12 dígitos
    private static final Pattern CEDULA_PATTERN =
            Pattern.compile("^\\d{6,12}$");
    // Detecta emojis y símbolos Unicode raros
    private static final Pattern EMOJI_PATTERN =
            Pattern.compile("[\\p{So}\\p{Cn}]|[\\uD83C-\\uD83E][\\uDC00-\\uDFFF]|[\\u2600-\\u27BF]");
    // Detecta caracteres de inyección XSS / SQL básicos
    private static final Pattern CARACTERES_PELIGROSOS =
            Pattern.compile("[<>'\"`;\\\\]");

    // ============================================================
    // PUNTO DE ENTRADA PRINCIPAL
    // ============================================================
    public Usuario registrar(Usuario usuario) {

        // 1. Campos obligatorios presentes
        validarCamposObligatorios(usuario);

        // 2. Seguridad: sin emojis ni caracteres peligrosos en ningún campo de texto
        validarSeguridadTexto("nombres", usuario.getNombres());
        validarSeguridadTexto("apellidos", usuario.getApellidos());
        validarSeguridadTexto("telefono", usuario.getTelefono());
        validarSeguridadTexto("correo_electronico", usuario.getCorreoElectronico());
        validarSeguridadTexto("cedula", usuario.getCedula());

        // 3. Formato y estructura de cada campo
        validarFormatoNombre("nombres", usuario.getNombres());
        validarFormatoNombre("apellidos", usuario.getApellidos());
        validarFormatoTelefono(usuario.getTelefono());
        validarFormatoCedula(usuario.getCedula());
        validarMayorDeEdad(usuario.getFechaNacimiento());

        // 4. Normalización y capitalización automática
        String nombresNormalizados     = capitalizarNombrePropio(usuario.getNombres());
        String apellidosNormalizados   = capitalizarNombrePropio(usuario.getApellidos());
        String correoNormalizado       = usuario.getCorreoElectronico().trim().toLowerCase();
        String telefonoNormalizado     = usuario.getTelefono().trim();
        String cedulaNormalizada       = usuario.getCedula().trim();
        String direccionNormalizada    = sanitizarDireccion(usuario.getDireccion());

        // 5. Validaciones de unicidad (correo y cédula irrepetibles)
        validarFormatoCorreo(correoNormalizado);
        validarCorreoUnico(correoNormalizado);
        validarCedulaUnica(cedulaNormalizada);

        // 6. Salario validado y redondeado a 2 decimales (pesos colombianos)
        BigDecimal salarioNormalizado = validarRangoSalario(usuario.getSalarioBase());

        // 7. Construir el objeto listo para persistir
        Usuario usuarioAGuardar = usuario.toBuilder()
                .cedula(cedulaNormalizada)
                .nombres(nombresNormalizados)
                .apellidos(apellidosNormalizados)
                .correoElectronico(correoNormalizado)
                .telefono(telefonoNormalizado)
                .direccion(direccionNormalizada)
                .salarioBase(salarioNormalizado)
                .estado(ESTADO_INICIAL)
                .fechaCreacion(LocalDateTime.now())   // Fecha/hora exacta del servidor
                .build();

        return usuarioRepository.guardar(usuarioAGuardar);
    }

    // ============================================================
    // VALIDACIONES PRIVADAS
    // ============================================================

    private void validarCamposObligatorios(Usuario usuario) {
        if (esNuloOVacio(usuario.getCedula()))
            throw new BusinessException("El campo 'cedula' es obligatorio");
        if (esNuloOVacio(usuario.getNombres()))
            throw new BusinessException("El campo 'nombres' es obligatorio y no puede estar vacío");
        if (esNuloOVacio(usuario.getApellidos()))
            throw new BusinessException("El campo 'apellidos' es obligatorio y no puede estar vacío");
        if (esNuloOVacio(usuario.getCorreoElectronico()))
            throw new BusinessException("El campo 'correo_electronico' es obligatorio y no puede estar vacío");
        if (usuario.getSalarioBase() == null)
            throw new BusinessException("El campo 'salario_base' es obligatorio");
        if (usuario.getFechaNacimiento() == null)
            throw new BusinessException("El campo 'fecha_nacimiento' es obligatorio");
        if (esNuloOVacio(usuario.getTelefono()))
            throw new BusinessException("El campo 'telefono' es obligatorio y no puede estar vacío");
    }

    private boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /** Rechaza emojis y caracteres peligrosos (XSS/SQL) */
    private void validarSeguridadTexto(String campo, String valor) {
        if (valor == null) return;
        if (EMOJI_PATTERN.matcher(valor).find())
            throw new BusinessException("El campo '" + campo + "' no puede contener emojis ni símbolos especiales");
        if (CARACTERES_PELIGROSOS.matcher(valor).find())
            throw new BusinessException("El campo '" + campo + "' contiene caracteres no permitidos (< > ' \" ; ` \\)");
    }

    /** Solo letras, longitud 2-100 */
    private void validarFormatoNombre(String campo, String valor) {
        String limpio = valor.trim();
        if (limpio.length() < 2 || limpio.length() > 100)
            throw new BusinessException("El campo '" + campo + "' debe tener entre 2 y 100 caracteres");
        if (!NOMBRE_PATTERN.matcher(limpio).matches())
            throw new BusinessException("El campo '" + campo + "' solo puede contener letras (sin números ni símbolos)");
    }

    /** Cédula: 6 a 12 dígitos, solo números */
    private void validarFormatoCedula(String cedula) {
        String limpia = cedula.trim();
        if (!CEDULA_PATTERN.matcher(limpia).matches())
            throw new BusinessException("La cédula debe contener entre 6 y 12 dígitos numéricos sin letras ni caracteres especiales");
    }

    /** Capitaliza cada palabra: 'juan carlos' → 'Juan Carlos' */
    private String capitalizarNombrePropio(String texto) {
        if (texto == null || texto.trim().isEmpty()) return texto;
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                sb.append(Character.toUpperCase(palabra.charAt(0)));
                if (palabra.length() > 1)
                    sb.append(palabra.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    /** Limpia la dirección y aplica restricciones de seguridad */
    private String sanitizarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) return null;
        String limpia = direccion.trim();
        if (limpia.length() > 200)
            throw new BusinessException("La dirección no puede exceder los 200 caracteres");
        if (CARACTERES_PELIGROSOS.matcher(limpia).find())
            throw new BusinessException("La dirección contiene caracteres no permitidos");
        if (EMOJI_PATTERN.matcher(limpia).find())
            throw new BusinessException("La dirección no puede contener emojis");
        return limpia;
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        if (!fechaNacimiento.isBefore(LocalDate.now()))
            throw new BusinessException("La fecha de nacimiento debe ser anterior a la fecha actual");
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < EDAD_MINIMA)
            throw new BusinessException("El solicitante debe ser mayor de edad (mínimo 18 años)");
        if (edad > EDAD_MAXIMA)
            throw new BusinessException("La edad registrada no puede superar los 120 años");
    }

    private void validarFormatoTelefono(String telefono) {
        if (!TELEFONO_PATTERN.matcher(telefono.trim()).matches())
            throw new BusinessException("El teléfono debe contener exactamente 10 dígitos numéricos sin letras, espacios ni caracteres especiales");
    }

    private void validarFormatoCorreo(String correo) {
        if (correo.length() > 150)
            throw new BusinessException("El correo electrónico no puede superar los 150 caracteres");
        if (!EMAIL_PATTERN.matcher(correo).matches())
            throw new BusinessException("El correo electrónico no tiene un formato válido (ejemplo: usuario@correo.com)");
    }

    /** Valida rango y normaliza a 2 decimales (pesos colombianos) */
    private BigDecimal validarRangoSalario(BigDecimal salario) {
        if (salario.compareTo(SALARIO_MINIMO) < 0 || salario.compareTo(SALARIO_MAXIMO) > 0)
            throw new BusinessException("El salario base debe estar entre $0 y $15.000.000 COP");
        return salario.setScale(2, RoundingMode.HALF_UP);
    }

    private void validarCorreoUnico(String correo) {
        if (usuarioRepository.existePorCorreoElectronico(correo))
            throw new BusinessException("El correo electrónico ya se encuentra registrado por otro solicitante");
    }

    private void validarCedulaUnica(String cedula) {
        if (usuarioRepository.existePorCedula(cedula))
            throw new BusinessException("La cédula ingresada ya se encuentra registrada en el sistema");
    }
}
