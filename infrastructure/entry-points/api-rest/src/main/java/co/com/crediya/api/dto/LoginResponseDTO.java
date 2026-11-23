package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.SesionUsuario;

import java.time.LocalDateTime;

/**
 * Contrato de respuesta para la confirmacion de autenticacion con token expedido.
 */
public class LoginResponseDTO {

    private String mensaje;
    private String token;
    private String tipoToken;
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String rol;
    private LocalDateTime timestamp;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String mensaje, String token, String tipoToken, Long idUsuario,
                            String nombres, String apellidos, String correoElectronico,
                            String rol, LocalDateTime timestamp) {
        this.mensaje = mensaje;
        this.token = token;
        this.tipoToken = tipoToken;
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.rol = rol;
        this.timestamp = timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static LoginResponseDTO exitoso(SesionUsuario sesion) {
        return new LoginResponseDTO(
                "Autenticacion exitosa",
                sesion.getToken(),
                sesion.getTipoToken(),
                sesion.getUsuario().getId(),
                sesion.getUsuario().getNombres(),
                sesion.getUsuario().getApellidos(),
                sesion.getUsuario().getCorreoElectronico(),
                sesion.getUsuario().getRol() != null ? sesion.getUsuario().getRol().name() : null,
                LocalDateTime.now()
        );
    }
}
