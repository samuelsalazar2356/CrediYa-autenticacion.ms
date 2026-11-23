package co.com.crediya.model.solicitud;

import java.math.BigDecimal;

/**
 * Proyeccion de dominio enriquecida para la revision manual de solicitudes por parte de un Asesor.
 */
public class SolicitudRevisionDetalle {

    private final Long idSolicitud;
    private final BigDecimal monto;
    private final Integer plazo;
    private final String email;
    private final String nombre;
    private final String tipoPrestamo;
    private final BigDecimal tasaInteres;
    private final String estadoSolicitud;
    private final BigDecimal salarioBase;
    private final BigDecimal deudaTotalMensualSolicitudesAprobadas;

    public SolicitudRevisionDetalle(Long idSolicitud, BigDecimal monto, Integer plazo,
                                    String email, String nombre, String tipoPrestamo,
                                    BigDecimal tasaInteres, String estadoSolicitud,
                                    BigDecimal salarioBase,
                                    BigDecimal deudaTotalMensualSolicitudesAprobadas) {
        this.idSolicitud = idSolicitud;
        this.monto = monto;
        this.plazo = plazo;
        this.email = email;
        this.nombre = nombre;
        this.tipoPrestamo = tipoPrestamo;
        this.tasaInteres = tasaInteres;
        this.estadoSolicitud = estadoSolicitud;
        this.salarioBase = salarioBase;
        this.deudaTotalMensualSolicitudesAprobadas = deudaTotalMensualSolicitudesAprobadas;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public BigDecimal getDeudaTotalMensualSolicitudesAprobadas() {
        return deudaTotalMensualSolicitudesAprobadas;
    }
}
