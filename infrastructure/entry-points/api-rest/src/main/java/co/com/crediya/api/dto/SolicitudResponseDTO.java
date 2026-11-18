package co.com.crediya.api.dto;

import co.com.crediya.model.solicitud.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Contrato de respuesta para la confirmacion de radicacion de solicitudes.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public String getMensaje() {
        return mensaje;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getMontoFormateado() {
        return montoFormateado;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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
