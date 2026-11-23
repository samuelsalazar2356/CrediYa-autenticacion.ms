package co.com.crediya.api.dto;

import co.com.crediya.model.solicitud.SolicitudRevisionDetalle;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Representacion DTO para cada elemento en la bandeja de revision manual del Asesor.
 */
public class SolicitudItemRevisionDTO {

    private Long idSolicitud;
    private BigDecimal monto;
    private String montoFormateado;
    private Integer plazo;
    private String email;
    private String nombre;
    private String tipoPrestamo;
    private BigDecimal tasaInteres;
    private String tasaInteresFormateada;
    private String estadoSolicitud;
    private BigDecimal salarioBase;
    private String salarioBaseFormateado;
    private BigDecimal deudaTotalMensualSolicitudesAprobadas;
    private String deudaTotalMensualFormateada;

    public SolicitudItemRevisionDTO() {
    }

    public SolicitudItemRevisionDTO(Long idSolicitud, BigDecimal monto, String montoFormateado,
                                    Integer plazo, String email, String nombre,
                                    String tipoPrestamo, BigDecimal tasaInteres,
                                    String tasaInteresFormateada, String estadoSolicitud,
                                    BigDecimal salarioBase, String salarioBaseFormateado,
                                    BigDecimal deudaTotalMensualSolicitudesAprobadas,
                                    String deudaTotalMensualFormateada) {
        this.idSolicitud = idSolicitud;
        this.monto = monto;
        this.montoFormateado = montoFormateado;
        this.plazo = plazo;
        this.email = email;
        this.nombre = nombre;
        this.tipoPrestamo = tipoPrestamo;
        this.tasaInteres = tasaInteres;
        this.tasaInteresFormateada = tasaInteresFormateada;
        this.estadoSolicitud = estadoSolicitud;
        this.salarioBase = salarioBase;
        this.salarioBaseFormateado = salarioBaseFormateado;
        this.deudaTotalMensualSolicitudesAprobadas = deudaTotalMensualSolicitudesAprobadas;
        this.deudaTotalMensualFormateada = deudaTotalMensualFormateada;
    }

    public static SolicitudItemRevisionDTO desdeDominio(SolicitudRevisionDetalle detalle) {
        NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        formatoCOP.setMaximumFractionDigits(2);

        String montoFormateado = detalle.getMonto() != null ? formatoCOP.format(detalle.getMonto()) : null;
        String salarioFormateado = detalle.getSalarioBase() != null ? formatoCOP.format(detalle.getSalarioBase()) : null;
        String deudaFormateada = detalle.getDeudaTotalMensualSolicitudesAprobadas() != null
                ? formatoCOP.format(detalle.getDeudaTotalMensualSolicitudesAprobadas())
                : null;

        String tasaFormateada = detalle.getTasaInteres() != null
                ? detalle.getTasaInteres().multiply(new BigDecimal("100")).setScale(2, java.math.RoundingMode.HALF_UP) + "%"
                : null;

        return new SolicitudItemRevisionDTO(
                detalle.getIdSolicitud(),
                detalle.getMonto(),
                montoFormateado,
                detalle.getPlazo(),
                detalle.getEmail(),
                detalle.getNombre(),
                detalle.getTipoPrestamo(),
                detalle.getTasaInteres(),
                tasaFormateada,
                detalle.getEstadoSolicitud(),
                detalle.getSalarioBase(),
                salarioFormateado,
                detalle.getDeudaTotalMensualSolicitudesAprobadas(),
                deudaFormateada
        );
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMontoFormateado() {
        return montoFormateado;
    }

    public void setMontoFormateado(String montoFormateado) {
        this.montoFormateado = montoFormateado;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public String getTasaInteresFormateada() {
        return tasaInteresFormateada;
    }

    public void setTasaInteresFormateada(String tasaInteresFormateada) {
        this.tasaInteresFormateada = tasaInteresFormateada;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public String getSalarioBaseFormateado() {
        return salarioBaseFormateado;
    }

    public void setSalarioBaseFormateado(String salarioBaseFormateado) {
        this.salarioBaseFormateado = salarioBaseFormateado;
    }

    public BigDecimal getDeudaTotalMensualSolicitudesAprobadas() {
        return deudaTotalMensualSolicitudesAprobadas;
    }

    public void setDeudaTotalMensualSolicitudesAprobadas(BigDecimal deudaTotalMensualSolicitudesAprobadas) {
        this.deudaTotalMensualSolicitudesAprobadas = deudaTotalMensualSolicitudesAprobadas;
    }

    public String getDeudaTotalMensualFormateada() {
        return deudaTotalMensualFormateada;
    }

    public void setDeudaTotalMensualFormateada(String deudaTotalMensualFormateada) {
        this.deudaTotalMensualFormateada = deudaTotalMensualFormateada;
    }
}
