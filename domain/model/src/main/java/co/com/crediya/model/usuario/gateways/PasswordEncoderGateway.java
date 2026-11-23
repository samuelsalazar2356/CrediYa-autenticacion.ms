package co.com.crediya.model.usuario.gateways;

/**
 * Puerto para el cifrado y validacion unidireccional de contrasenas.
 * Desacopla la logica de negocio del algoritmo de hashing implementado en infraestructura.
 */
public interface PasswordEncoderGateway {

    String codificar(String contrasenaPlana);

    boolean verificar(String contrasenaPlana, String contrasenaCodificada);
}
