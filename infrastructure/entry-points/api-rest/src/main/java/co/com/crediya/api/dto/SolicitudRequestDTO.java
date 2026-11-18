package co.com.crediya.api.dto;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.TipoPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Contrato de entrada para la radicacion de solicitudes de credito.
 * Aplica estrategia fail-fast para desacoplar el protocolo HTTP del dominio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRequestDTO {

    private String documentoIdentidad;
    private BigDecimal monto;
    private Integer plazo;
    private String tipoPrestamo;

    /**
     * Transforma el DTO de transporte al modelo de dominio.
     *
     * @return Entidad Solicitud lista para ser evaluada por el caso de uso.
     */
    public Solicitud toDomain() {
        TipoPrestamo tipoResuelto = this.tipoPrestamo != null ? TipoPrestamo.fromString(this.tipoPrestamo) : null;

        return Solicitud.builder()
                .documentoIdentidad(this.documentoIdentidad)
                .monto(this.monto)
                .plazoMeses(this.plazo)
                .tipoPrestamo(tipoResuelto)
                .build();
    }
}
