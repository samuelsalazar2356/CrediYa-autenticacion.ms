package co.com.crediya.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad central del dominio para la gestion de solicitudes de credito.
 * Invariante: No depende de frameworks ni mecanismos de persistencia.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private Long id;
    private String documentoIdentidad;
    private BigDecimal monto;
    private Integer plazoMeses;
    private TipoPrestamo tipoPrestamo;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public TipoPrestamo getTipoPrestamo() {
        return tipoPrestamo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
