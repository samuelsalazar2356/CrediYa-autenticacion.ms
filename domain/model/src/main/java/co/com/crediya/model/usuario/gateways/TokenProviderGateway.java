package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.TokenClaims;
import co.com.crediya.model.usuario.Usuario;

/**
 * Puerto para la generacion y validacion de tokens de autenticacion (JWT).
 */
public interface TokenProviderGateway {

    String generarToken(Usuario usuario);

    TokenClaims validarToken(String token);
}
