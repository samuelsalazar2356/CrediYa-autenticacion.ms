package co.com.crediya.api.dto;

import co.com.crediya.model.solicitud.Solicitud;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Contrato de respuesta para la confirmacion de radicacion de solicitudes.
 */
public class SolicitudResponseDTO {

    private String mensaje;
    private Long idSolicitud;
    private String documentoIdentidad;
    private BigDecimal monto;
    private String montoFormateado;
    private Integer plazoMeses;
    private String tipoPrestamo;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime timestamp;

    public SolicitudResponseDTO() {
    }

    public SolicitudResponseDTO(String mensaje, Long idSolicitud, String documentoIdentidad,
                                BigDecimal monto, String montoFormateado, Integer plazoMeses,
                                String tipoPrestamo, String estado, LocalDateTime fechaCreacion,
                                LocalDateTime timestamp) {
        this.mensaje = mensaje;
        this.idSolicitud = idSolicitud;
        this.documentoIdentidad = documentoIdentidad;
        this.monto = monto;
        this.montoFormateado = montoFormateado;
        this.plazoMeses = plazoMeses;
        this.tipoPrestamo = tipoPrestamo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.timestamp = timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
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

    public String getMontoFormateado() {
        return montoFormateado;
    }

    public void setMontoFormateado(String montoFormateado) {
        this.montoFormateado = montoFormateado;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String tipoPrestamo) {
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static SolicitudResponseDTOBuilder builder() {
        return new SolicitudResponseDTOBuilder();
    }

    public static class SolicitudResponseDTOBuilder {
        private String mensaje;
        private Long idSolicitud;
        private String documentoIdentidad;
        private BigDecimal monto;
        private String montoFormateado;
        private Integer plazoMeses;
        private String tipoPrestamo;
        private String estado;
        private LocalDateTime fechaCreacion;
        private LocalDateTime timestamp;

        public SolicitudResponseDTOBuilder mensaje(String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public SolicitudResponseDTOBuilder idSolicitud(Long idSolicitud) {
            this.idSolicitud = idSolicitud;
            return this;
        }

        public SolicitudResponseDTOBuilder documentoIdentidad(String documentoIdentidad) {
            this.documentoIdentidad = documentoIdentidad;
            return this;
        }

        public SolicitudResponseDTOBuilder monto(BigDecimal monto) {
            this.monto = monto;
            return this;
        }

        public SolicitudResponseDTOBuilder montoFormateado(String montoFormateado) {
            this.montoFormateado = montoFormateado;
            return this;
        }

        public SolicitudResponseDTOBuilder plazoMeses(Integer plazoMeses) {
            this.plazoMeses = plazoMeses;
            return this;
        }

        public SolicitudResponseDTOBuilder tipoPrestamo(String tipoPrestamo) {
            this.tipoPrestamo = tipoPrestamo;
            return this;
        }

        public SolicitudResponseDTOBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public SolicitudResponseDTOBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public SolicitudResponseDTOBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public SolicitudResponseDTO build() {
            return new SolicitudResponseDTO(mensaje, idSolicitud, documentoIdentidad,
                    monto, montoFormateado, plazoMeses, tipoPrestamo, estado, fechaCreacion, timestamp);
        }
    }

    public static SolicitudResponseDTO exitoso(Solicitud solicitud) {
        String montoFormateado = null;
        if (solicitud.getMonto() != null) {
            NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            formatoCOP.setMaximumFractionDigits(2);
            montoFormateado = formatoCOP.format(solicitud.getMonto());
        }

        return SolicitudResponseDTO.builder()
                .mensaje("Solicitud de prestamo radicada exitosamente")
                .idSolicitud(solicitud.getId())
                .documentoIdentidad(solicitud.getDocumentoIdentidad())
                .monto(solicitud.getMonto())
                .montoFormateado(montoFormateado)
                .plazoMeses(solicitud.getPlazoMeses())
                .tipoPrestamo(solicitud.getTipoPrestamo() != null ? solicitud.getTipoPrestamo().getDescripcion() : null)
                .estado(solicitud.getEstado())
                .fechaCreacion(solicitud.getFechaCreacion())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
