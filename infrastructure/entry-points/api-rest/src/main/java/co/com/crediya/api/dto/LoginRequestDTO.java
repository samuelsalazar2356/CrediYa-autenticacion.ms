package co.com.crediya.api.dto;

/**
 * Contrato de entrada para la autenticacion de usuarios.
 */
public class LoginRequestDTO {

    private String correoElectronico;
    private String contrasena;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String correoElectronico, String contrasena) {
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
