package co.com.crediya.model.solicitud;

import co.com.crediya.model.usuario.excepcion.BusinessException;

import java.util.Arrays;

/**
 * Catalogo de tipos de prestamo admitidos por las politicas de credito.
 * Invariante: Un prestamo solo puede registrarse bajo una modalidad formalmente aprobada.
 */
public enum TipoPrestamo {
    PERSONAL("Personal"),
    VEHICULO("Vehiculo"),
    VIVIENDA("Vivienda"),
    LIBRE_INVERSION("Libre Inversion"),
    EDUCATIVO("Educativo");

    private final String descripcion;

    TipoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
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
