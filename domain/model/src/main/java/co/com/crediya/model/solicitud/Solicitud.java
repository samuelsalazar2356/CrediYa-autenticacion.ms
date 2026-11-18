package co.com.crediya.model.solicitud;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad central del dominio para la gestion de solicitudes de credito.
 * Invariante: Modelo de dominio puro, sin acoplamiento a frameworks externos.
 */
public class Solicitud {

    private Long id;
    private String documentoIdentidad;
    private BigDecimal monto;
    private Integer plazoMeses;
    private TipoPrestamo tipoPrestamo;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Solicitud() {
    }

    public Solicitud(Long id, String documentoIdentidad, BigDecimal monto, Integer plazoMeses,
                     TipoPrestamo tipoPrestamo, String estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.documentoIdentidad = documentoIdentidad;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.tipoPrestamo = tipoPrestamo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public TipoPrestamo getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(TipoPrestamo tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public static SolicitudBuilder builder() {
        return new SolicitudBuilder();
    }

    public SolicitudBuilder toBuilder() {
        return new SolicitudBuilder()
                .id(this.id)
                .documentoIdentidad(this.documentoIdentidad)
                .monto(this.monto)
                .plazoMeses(this.plazoMeses)
                .tipoPrestamo(this.tipoPrestamo)
                .estado(this.estado)
                .fechaCreacion(this.fechaCreacion);
    }

    public static class SolicitudBuilder {
        private Long id;
        private String documentoIdentidad;
        private BigDecimal monto;
        private Integer plazoMeses;
        private TipoPrestamo tipoPrestamo;
        private String estado;
        private LocalDateTime fechaCreacion;

        public SolicitudBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SolicitudBuilder documentoIdentidad(String documentoIdentidad) {
            this.documentoIdentidad = documentoIdentidad;
            return this;
        }

        public SolicitudBuilder monto(BigDecimal monto) {
            this.monto = monto;
            return this;
        }

        public SolicitudBuilder plazoMeses(Integer plazoMeses) {
            this.plazoMeses = plazoMeses;
            return this;
        }

        public SolicitudBuilder tipoPrestamo(TipoPrestamo tipoPrestamo) {
            this.tipoPrestamo = tipoPrestamo;
            return this;
        }

        public SolicitudBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public SolicitudBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Solicitud build() {
            return new Solicitud(id, documentoIdentidad, monto, plazoMeses, tipoPrestamo, estado, fechaCreacion);
        }
    }
}
