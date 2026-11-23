package co.com.crediya.model.usuario.gateways;

import java.time.LocalDateTime;

/**
 * Puerto de salida para la gestion de revocacion de tokens de autenticacion.
 * Permite invalidar tokens antes de su tiempo natural de expiracion (ej. logout o revocacion administrativa).
 */
public interface TokenRevocationGateway {

    void revocarToken(String token, LocalDateTime expiracion);

    boolean estaRevocado(String token);
}
