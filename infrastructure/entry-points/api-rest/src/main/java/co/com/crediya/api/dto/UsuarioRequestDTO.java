package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;

    public Usuario toDomain() {
        return Usuario.builder()
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .direccion(this.direccion)
                .telefono(this.telefono)
                .correoElectronico(this.correoElectronico)
                .salarioBase(this.salarioBase)
                .build();
    }
}
