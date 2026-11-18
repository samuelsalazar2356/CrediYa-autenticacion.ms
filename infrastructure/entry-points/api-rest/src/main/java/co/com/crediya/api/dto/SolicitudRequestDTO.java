package co.com.crediya.api.dto;

import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.TipoPrestamo;

import java.math.BigDecimal;

/**
 * Contrato de entrada para la radicacion de solicitudes de credito.
 * Aplica estrategia fail-fast para desacoplar el protocolo HTTP del dominio.
 */
public class SolicitudRequestDTO {

    private String documentoIdentidad;
    private BigDecimal monto;
    private Integer plazo;
    private String tipoPrestamo;

    public SolicitudRequestDTO() {
    }

    public SolicitudRequestDTO(String documentoIdentidad, BigDecimal monto, Integer plazo, String tipoPrestamo) {
        this.documentoIdentidad = documentoIdentidad;
        this.monto = monto;
        this.plazo = plazo;
        this.tipoPrestamo = tipoPrestamo;
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

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public static SolicitudRequestDTOBuilder builder() {
        return new SolicitudRequestDTOBuilder();
    }

    public static class SolicitudRequestDTOBuilder {
        private String documentoIdentidad;
        private BigDecimal monto;
        private Integer plazo;
        private String tipoPrestamo;

        public SolicitudRequestDTOBuilder documentoIdentidad(String documentoIdentidad) {
            this.documentoIdentidad = documentoIdentidad;
            return this;
        }

        public SolicitudRequestDTOBuilder monto(BigDecimal monto) {
            this.monto = monto;
            return this;
        }

        public SolicitudRequestDTOBuilder plazo(Integer plazo) {
            this.plazo = plazo;
            return this;
        }

        public SolicitudRequestDTOBuilder tipoPrestamo(String tipoPrestamo) {
            this.tipoPrestamo = tipoPrestamo;
            return this;
        }

        public SolicitudRequestDTO build() {
            return new SolicitudRequestDTO(documentoIdentidad, monto, plazo, tipoPrestamo);
        }
    }

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
