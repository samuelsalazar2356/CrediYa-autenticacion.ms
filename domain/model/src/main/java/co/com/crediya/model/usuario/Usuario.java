package co.com.crediya.model.usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad central del dominio para la gestion de usuarios del sistema.
 * Invariante: Modelo de dominio puro, sin acoplamiento a frameworks externos.
 */
public class Usuario {

    private Long id;
    private String cedula;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Usuario() {
    }

    public Usuario(Long id, String cedula, String nombres, String apellidos,
                   LocalDate fechaNacimiento, String direccion, String telefono,
                   String correoElectronico, BigDecimal salarioBase,
                   String estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.salarioBase = salarioBase;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
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

    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    public UsuarioBuilder toBuilder() {
        return new UsuarioBuilder()
                .id(this.id)
                .cedula(this.cedula)
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .fechaNacimiento(this.fechaNacimiento)
                .direccion(this.direccion)
                .telefono(this.telefono)
                .correoElectronico(this.correoElectronico)
                .salarioBase(this.salarioBase)
                .estado(this.estado)
                .fechaCreacion(this.fechaCreacion);
    }

    public static class UsuarioBuilder {
        private Long id;
        private String cedula;
        private String nombres;
        private String apellidos;
        private LocalDate fechaNacimiento;
        private String direccion;
        private String telefono;
        private String correoElectronico;
        private BigDecimal salarioBase;
        private String estado;
        private LocalDateTime fechaCreacion;

        public UsuarioBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UsuarioBuilder cedula(String cedula) {
            this.cedula = cedula;
            return this;
        }

        public UsuarioBuilder nombres(String nombres) {
            this.nombres = nombres;
            return this;
        }

        public UsuarioBuilder apellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public UsuarioBuilder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public UsuarioBuilder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public UsuarioBuilder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public UsuarioBuilder correoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
            return this;
        }

        public UsuarioBuilder salarioBase(BigDecimal salarioBase) {
            this.salarioBase = salarioBase;
            return this;
        }

        public UsuarioBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public UsuarioBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Usuario build() {
            return new Usuario(id, cedula, nombres, apellidos, fechaNacimiento,
                    direccion, telefono, correoElectronico, salarioBase, estado, fechaCreacion);
        }
    }
}
