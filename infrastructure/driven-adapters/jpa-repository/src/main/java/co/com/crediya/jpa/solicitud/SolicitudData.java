package co.com.crediya.jpa.solicitud;

import co.com.crediya.model.solicitud.TipoPrestamo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representacion relacional de la tabla solicitudes en PostgreSQL.
 * Incluye indice sobre documentoidentidad para optimizar consultas de evaluacion crediticia.
 */
@Entity
@Table(name = "solicitudes", indexes = {
        @Index(name = "idx_solicitudes_documento", columnList = "documentoidentidad")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documentoidentidad", nullable = false, length = 12)
    private String documentoIdentidad;

    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "plazomeses", nullable = false)
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoprestamo", nullable = false, length = 50)
    private TipoPrestamo tipoPrestamo;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "fechacreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.estado == null || this.estado.trim().isEmpty()) {
            this.estado = "Pendiente de revisión";
        }
    }
}
