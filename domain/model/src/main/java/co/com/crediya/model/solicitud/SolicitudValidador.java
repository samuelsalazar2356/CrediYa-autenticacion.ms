package co.com.crediya.model.solicitud;

import co.com.crediya.model.usuario.excepcion.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

/**
 * Validador de invariantes y normalizador del agregado Solicitud.
 * Centraliza las reglas de negocio defensivas previas a la persistencia.
 */
public final class SolicitudValidador {

    private static final BigDecimal MONTO_MINIMO = new BigDecimal("100000.00");
    private static final BigDecimal MONTO_MAXIMO = new BigDecimal("500000000.00");
    private static final int PLAZO_MINIMO_MESES = 1;
    private static final int PLAZO_MAXIMO_MESES = 120;

    private static final Pattern DOCUMENTO_PATTERN = Pattern.compile("^\\d{6,12}$");
    private static final Pattern CARACTERES_PELIGROSOS = Pattern.compile("[<>'\"`;\\\\]");
    private static final Pattern EMOJI_PATTERN =
            Pattern.compile("[\\p{So}\\p{Cn}]|[\\uD83C-\\uD83E][\\uDC00-\\uDFFF]|[\\u2600-\\u27BF]");

    private SolicitudValidador() {
        // Clase utilitaria sin estado
    }

    /**
     * Valida la estructura y coherencia financiera de la solicitud antes de admitirla.
     *
     * @param solicitud Entidad candidata a registro.
     * @return Entidad con valores saneados y escala monetaria estandarizada.
     */
    public static Solicitud validarYNormalizar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new BusinessException("La solicitud no puede ser nula");
        }

        String documentoNormalizado = validarYNormalizarDocumento(solicitud.getDocumentoIdentidad());
        BigDecimal montoNormalizado = validarYNormalizarMonto(solicitud.getMonto());
        Integer plazoNormalizado = validarPlazo(solicitud.getPlazoMeses());

        if (solicitud.getTipoPrestamo() == null) {
            throw new BusinessException("El tipo de prestamo es obligatorio y debe existir en el sistema");
        }

        return solicitud.toBuilder()
                .documentoIdentidad(documentoNormalizado)
                .monto(montoNormalizado)
                .plazoMeses(plazoNormalizado)
                .build();
    }

    private static String validarYNormalizarDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new BusinessException("El documento de identidad es obligatorio");
        }

        String documentoLimpio = documento.trim();

        if (EMOJI_PATTERN.matcher(documentoLimpio).find()) {
            throw new BusinessException("El documento de identidad no puede contener emojis ni simbolos graficos");
        }

        if (CARACTERES_PELIGROSOS.matcher(documentoLimpio).find()) {
            throw new BusinessException("El documento de identidad contiene caracteres no permitidos");
        }

        if (!DOCUMENTO_PATTERN.matcher(documentoLimpio).matches()) {
            throw new BusinessException("El documento de identidad debe contener entre 6 y 12 digitos numericos");
        }

        return documentoLimpio;
    }

    private static BigDecimal validarYNormalizarMonto(BigDecimal monto) {
        if (monto == null) {
            throw new BusinessException("El monto del prestamo es obligatorio");
        }

        // Se exige escala monetaria estandarizada de 2 decimales para evitar desalineaciones contables
        BigDecimal montoEscalado = monto.setScale(2, RoundingMode.HALF_UP);

        if (montoEscalado.compareTo(MONTO_MINIMO) < 0) {
            throw new BusinessException("El monto minimo de solicitud es $100.000,00 COP");
        }

        if (montoEscalado.compareTo(MONTO_MAXIMO) > 0) {
            throw new BusinessException("El monto maximo de solicitud no puede exceder $500.000.000,00 COP");
        }

        return montoEscalado;
    }

    private static Integer validarPlazo(Integer plazo) {
        if (plazo == null) {
            throw new BusinessException("El plazo en meses es obligatorio");
        }

        if (plazo < PLAZO_MINIMO_MESES || plazo > PLAZO_MAXIMO_MESES) {
            throw new BusinessException("El plazo debe estar comprendido entre 1 y 120 meses");
        }

        return plazo;
    }
}
