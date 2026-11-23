package co.com.crediya.model.usuario;

import java.time.LocalDateTime;

/**
 * Objeto de valor inmutable que encapsula las afirmaciones (claims) del token de autenticacion.
 */
public class TokenClaims {

    private final Long usuarioId;
    private final String cedula;
    private final String correoElectronico;
    private final RolUsuario rol;
    private final LocalDateTime expiracion;

    public TokenClaims(Long usuarioId, String cedula, String correoElectronico, RolUsuario rol, LocalDateTime expiracion) {
        this.usuarioId = usuarioId;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.rol = rol;
        this.expiracion = expiracion;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public LocalDateTime getExpiracion() {
        return expiracion;
    }

    public boolean estaExpirado() {
        return expiracion != null && LocalDateTime.now().isAfter(expiracion);
    }
}
