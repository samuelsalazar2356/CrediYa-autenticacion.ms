package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Contrato de entrada para el registro de usuarios.
 * Aplica estrategia fail-fast para desacoplar el protocolo HTTP del dominio.
 */
public class UsuarioRequestDTO {

    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
    private String cedula;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String nombres, String apellidos, LocalDate fechaNacimiento,
                             String direccion, String telefono, String correoElectronico,
                             BigDecimal salarioBase, String cedula) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.salarioBase = salarioBase;
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public static UsuarioRequestDTOBuilder builder() {
        return new UsuarioRequestDTOBuilder();
    }

    public static class UsuarioRequestDTOBuilder {
        private String nombres;
        private String apellidos;
        private LocalDate fechaNacimiento;
        private String direccion;
        private String telefono;
        private String correoElectronico;
        private BigDecimal salarioBase;
        private String cedula;

        public UsuarioRequestDTOBuilder nombres(String nombres) {
            this.nombres = nombres;
            return this;
        }

        public UsuarioRequestDTOBuilder apellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public UsuarioRequestDTOBuilder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public UsuarioRequestDTOBuilder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public UsuarioRequestDTOBuilder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public UsuarioRequestDTOBuilder correoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
            return this;
        }

        public UsuarioRequestDTOBuilder salarioBase(BigDecimal salarioBase) {
            this.salarioBase = salarioBase;
            return this;
        }

        public UsuarioRequestDTOBuilder cedula(String cedula) {
            this.cedula = cedula;
            return this;
        }

        public UsuarioRequestDTO build() {
            return new UsuarioRequestDTO(nombres, apellidos, fechaNacimiento, direccion,
                    telefono, correoElectronico, salarioBase, cedula);
        }
    }

    /**
     * Transforma el DTO de transporte al modelo de dominio.
     *
     * @return Entidad Usuario lista para ser procesada por el caso de uso.
     */
    public Usuario toDomain() {
        return Usuario.builder()
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .direccion(this.direccion)
                .telefono(this.telefono)
                .correoElectronico(this.correoElectronico)
                .salarioBase(this.salarioBase)
                .cedula(this.cedula)
                .build();
    }
}
