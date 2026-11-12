package co.com.crediya.model.usuario;

import co.com.crediya.model.usuario.excepcion.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public final class UsuarioValidador {

    private static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");

    private static final Pattern CORREO_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^\\d{10}$");
    private static final Pattern NOMBRE_PATTERN =
            Pattern.compile("^[a-zA-Z\u00e1\u00e9\u00ed\u00f3\u00fa\u00c1\u00c9\u00cd\u00d3\u00da\u00f1\u00d1\u00fc\u00dc\\s'\\-]+$");
    private static final Pattern CEDULA_PATTERN =
            Pattern.compile("^\\d{6,12}$");
    private static final Pattern EMOJI_PATTERN =
            Pattern.compile("[\\p{So}\\p{Cn}]|[\\uD83C-\\uD83E][\\uDC00-\\uDFFF]|[\\u2600-\\u27BF]");
    private static final Pattern CARACTERES_PELIGROSOS =
            Pattern.compile("[<>'\"`;\\\\]");

    private UsuarioValidador() {
        // Constructor privado para clase utilitaria
    }

    /**
     * Valida reglas de formato, seguridad y normaliza los datos del usuario.
     * Retorna una nueva instancia limpia y estandarizada.
     */
    public static Usuario validarYNormalizar(Usuario usuario) {
        if (usuario == null) {
            throw new BusinessException("Los datos del usuario no pueden ser nulos");
        }

        validarCamposObligatorios(usuario);

        validarSeguridadTexto("nombres", usuario.getNombres());
        validarSeguridadTexto("apellidos", usuario.getApellidos());
        validarSeguridadTexto("telefono", usuario.getTelefono());
        validarSeguridadTexto("correo_electronico", usuario.getCorreoElectronico());
        validarSeguridadTexto("cedula", usuario.getCedula());

        validarFormatoNombre("nombres", usuario.getNombres());
        validarFormatoNombre("apellidos", usuario.getApellidos());
        validarFormatoTelefono(usuario.getTelefono());
        validarFormatoCedula(usuario.getCedula());
        validarMayorDeEdad(usuario.getFechaNacimiento());

        String nombresNormalizados   = capitalizarNombrePropio(usuario.getNombres());
        String apellidosNormalizados = capitalizarNombrePropio(usuario.getApellidos());
        String correoNormalizado     = usuario.getCorreoElectronico().trim().toLowerCase();
        String telefonoNormalizado   = usuario.getTelefono().trim();
        String cedulaNormalizada     = usuario.getCedula().trim();
        String direccionNormalizada  = sanitizarDireccion(usuario.getDireccion());

        validarFormatoCorreo(correoNormalizado);
        BigDecimal salarioNormalizado = validarRangoSalario(usuario.getSalarioBase());

        return usuario.toBuilder()
                .cedula(cedulaNormalizada)
                .nombres(nombresNormalizados)
                .apellidos(apellidosNormalizados)
                .correoElectronico(correoNormalizado)
                .telefono(telefonoNormalizado)
                .direccion(direccionNormalizada)
                .salarioBase(salarioNormalizado)
                .build();
    }

    private static void validarCamposObligatorios(Usuario usuario) {
        if (esNuloOVacio(usuario.getCedula()))
            throw new BusinessException("El campo 'cedula' es obligatorio");
        if (esNuloOVacio(usuario.getNombres()))
            throw new BusinessException("El campo 'nombres' es obligatorio y no puede estar vacío");
        if (esNuloOVacio(usuario.getApellidos()))
            throw new BusinessException("El campo 'apellidos' es obligatorio y no puede estar vacío");
        if (esNuloOVacio(usuario.getCorreoElectronico()))
            throw new BusinessException("El campo 'correo_electronico' es obligatorio y no puede estar vacío");
        if (usuario.getFechaNacimiento() == null)
            throw new BusinessException("El campo 'fecha_nacimiento' es obligatorio");
        if (usuario.getSalarioBase() == null)
            throw new BusinessException("El campo 'salario_base' es obligatorio");
        if (esNuloOVacio(usuario.getTelefono()))
            throw new BusinessException("El campo 'telefono' es obligatorio y no puede estar vacío");
    }

    private static void validarSeguridadTexto(String campo, String valor) {
        validarSinEmojis(campo, valor);
        validarSinInyeccion(campo, valor);
    }

    private static void validarSinEmojis(String campo, String valor) {
        if (EMOJI_PATTERN.matcher(valor).find()) {
            throw new BusinessException("El campo '" + campo + "' no puede contener emojis ni símbolos especiales");
        }
    }

    private static void validarSinInyeccion(String campo, String valor) {
        if (CARACTERES_PELIGROSOS.matcher(valor).find()) {
            throw new BusinessException("El campo '" + campo + "' contiene caracteres no permitidos");
        }
    }

    private static void validarFormatoNombre(String campo, String valor) {
        if (!NOMBRE_PATTERN.matcher(valor.trim()).matches()) {
            throw new BusinessException("El campo '" + campo + "' solo puede contener letras (sin números ni símbolos)");
        }
    }

    private static void validarFormatoCedula(String cedula) {
        if (!CEDULA_PATTERN.matcher(cedula.trim()).matches()) {
            throw new BusinessException("La cédula debe contener entre 6 y 12 dígitos numéricos sin letras ni caracteres especiales");
        }
    }

    private static String capitalizarNombrePropio(String texto) {
        if (texto == null) return null;
        String[] palabras = texto.trim().toLowerCase().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                         .append(palabra.substring(1))
                         .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    private static String sanitizarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) return null;
        String limpia = direccion.trim();
        if (limpia.length() > 200) {
            throw new BusinessException("La dirección no puede exceder los 200 caracteres");
        }
        if (CARACTERES_PELIGROSOS.matcher(limpia).find()) {
            throw new BusinessException("La dirección contiene caracteres no permitidos");
        }
        if (EMOJI_PATTERN.matcher(limpia).find()) {
            throw new BusinessException("La dirección no puede contener emojis");
        }
        return limpia;
    }

    private static void validarMayorDeEdad(LocalDate fechaNacimiento) {
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();
        if (edad < 18) {
            throw new BusinessException("El solicitante debe ser mayor de edad (mínimo 18 años)");
        }
        if (edad > 120) {
            throw new BusinessException("La edad registrada no puede superar los 120 años");
        }
    }

    private static void validarFormatoTelefono(String telefono) {
        if (!TELEFONO_PATTERN.matcher(telefono.trim()).matches()) {
            throw new BusinessException("El teléfono debe contener exactamente 10 dígitos numéricos sin letras, espacios ni caracteres especiales");
        }
    }

    private static void validarFormatoCorreo(String correo) {
        if (correo.length() > 150) {
            throw new BusinessException("El correo electrónico no puede superar los 150 caracteres");
        }
        if (!CORREO_PATTERN.matcher(correo).matches()) {
            throw new BusinessException("El correo electrónico no tiene un formato válido (ejemplo: usuario@correo.com)");
        }
    }

    private static BigDecimal validarRangoSalario(BigDecimal salario) {
        if (salario.compareTo(SALARIO_MINIMO) < 0 || salario.compareTo(SALARIO_MAXIMO) > 0)
            throw new BusinessException("El salario base debe estar entre $0 y $15.000.000 COP");
        return salario.setScale(2, RoundingMode.HALF_UP);
    }

    private static boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
