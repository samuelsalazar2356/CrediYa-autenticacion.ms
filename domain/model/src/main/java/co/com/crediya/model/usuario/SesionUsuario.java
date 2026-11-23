package co.com.crediya.model.usuario;

/**
 * Modelo de dominio que representa una sesion de usuario activa con su token generado.
 */
public class SesionUsuario {

    private final String token;
    private final String tipoToken;
    private final Usuario usuario;

    public SesionUsuario(String token, String tipoToken, Usuario usuario) {
        this.token = token;
        this.tipoToken = tipoToken;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
