package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.Usuario;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Contrato de respuesta para la confirmacion de registro de usuarios.
 */
public class UsuarioResponseDTO {

    private String mensaje;
    private Usuario usuario;
    private String salarioFormateado;
    private LocalDateTime timestamp;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(String mensaje, Usuario usuario, String salarioFormateado, LocalDateTime timestamp) {
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.salarioFormateado = salarioFormateado;
        this.timestamp = timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSalarioFormateado() {
        return salarioFormateado;
    }

    public void setSalarioFormateado(String salarioFormateado) {
        this.salarioFormateado = salarioFormateado;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static UsuarioResponseDTOBuilder builder() {
        return new UsuarioResponseDTOBuilder();
    }

    public static class UsuarioResponseDTOBuilder {
        private String mensaje;
        private Usuario usuario;
        private String salarioFormateado;
        private LocalDateTime timestamp;

        public UsuarioResponseDTOBuilder mensaje(String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public UsuarioResponseDTOBuilder usuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        public UsuarioResponseDTOBuilder salarioFormateado(String salarioFormateado) {
            this.salarioFormateado = salarioFormateado;
            return this;
        }

        public UsuarioResponseDTOBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public UsuarioResponseDTO build() {
            return new UsuarioResponseDTO(mensaje, usuario, salarioFormateado, timestamp);
        }
    }

    public static UsuarioResponseDTO exitoso(Usuario usuario) {
        String salarioFormateado = null;
        if (usuario.getSalarioBase() != null) {
            NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            formatoCOP.setMaximumFractionDigits(2);
            salarioFormateado = formatoCOP.format(usuario.getSalarioBase());
        }

        return UsuarioResponseDTO.builder()
                .mensaje("Usuario registrado exitosamente en el sistema CrediYa")
                .usuario(usuario)
                .salarioFormateado(salarioFormateado)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
