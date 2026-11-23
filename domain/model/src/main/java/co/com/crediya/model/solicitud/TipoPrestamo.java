package co.com.crediya.model.solicitud;

import co.com.crediya.model.usuario.excepcion.BusinessException;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Catalogo de tipos de prestamo admitidos por las politicas de credito.
 * Invariante: Un prestamo solo puede registrarse bajo una modalidad formalmente aprobada.
 */
public enum TipoPrestamo {
    PERSONAL("Personal", new BigDecimal("0.0150")),
    VEHICULO("Vehiculo", new BigDecimal("0.0120")),
    VIVIENDA("Vivienda", new BigDecimal("0.0090")),
    LIBRE_INVERSION("Libre Inversion", new BigDecimal("0.0180")),
    EDUCATIVO("Educativo", new BigDecimal("0.0100"));

    private final String descripcion;
    private final BigDecimal tasaInteresMensual;

    TipoPrestamo(String descripcion, BigDecimal tasaInteresMensual) {
        this.descripcion = descripcion;
        this.tasaInteresMensual = tasaInteresMensual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getTasaInteresMensual() {
        return tasaInteresMensual;
    }

    /**
     * Resuelve un tipo de prestamo a partir de una cadena normalizada.
     * Es insensible a mayusculas, minusculas y espacios accidentales.
     *
     * @param valor Cadena recibida en la solicitud.
     * @return Constante de TipoPrestamo coincidente.
     * @throws BusinessException Cuando el tipo de prestamo no existe en el catalogo.
     */
    public static TipoPrestamo fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new BusinessException("El tipo de prestamo es obligatorio");
        }

        String valorNormalizado = valor.trim().toUpperCase().replace(" ", "_");

        for (TipoPrestamo tipo : TipoPrestamo.values()) {
            if (tipo.name().equals(valorNormalizado) || tipo.descripcion.equalsIgnoreCase(valor.trim())) {
                return tipo;
            }
        }

        String tiposPermitidos = Arrays.toString(TipoPrestamo.values());
        throw new BusinessException("El tipo de prestamo '" + valor + "' no es valido. Tipos permitidos: " + tiposPermitidos);
    }
}
