package co.com.crediya.api;

import co.com.crediya.api.dto.LoginRequestDTO;
import co.com.crediya.api.dto.LoginResponseDTO;
import co.com.crediya.api.security.JwtAuthorizationFilter;
import co.com.crediya.model.usuario.SesionUsuario;
import co.com.crediya.model.usuario.TokenClaims;
import co.com.crediya.model.usuario.gateways.TokenRevocationGateway;
import co.com.crediya.usecase.autenticacion.AutenticarUsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Controlador REST para el inicio de sesion, expedicion de tokens y revocacion de sesion.
 * Expone endpoints POST /api/v1/login y POST /api/v1/login/logout.
 */
@RestController
@RequestMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginApiRest {

    private static final Logger log = LoggerFactory.getLogger(LoginApiRest.class);
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final TokenRevocationGateway tokenRevocationGateway;

    public LoginApiRest(AutenticarUsuarioUseCase autenticarUsuarioUseCase,
                        TokenRevocationGateway tokenRevocationGateway) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.tokenRevocationGateway = tokenRevocationGateway;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        log.info("Iniciando procesamiento de solicitud de autenticacion");

        SesionUsuario sesion = autenticarUsuarioUseCase.autenticar(
                requestDTO.getCorreoElectronico(),
                requestDTO.getContrasena()
        );

        log.info("Autenticacion exitosa para usuario ID: {} con rol: {}",
                sesion.getUsuario().getId(), sesion.getUsuario().getRol());

        return ResponseEntity.status(HttpStatus.OK).body(LoginResponseDTO.exitoso(sesion));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            TokenClaims claims = (TokenClaims) httpRequest.getAttribute(JwtAuthorizationFilter.CLAIMS_ATTRIBUTE);
            java.time.LocalDateTime expiracion = claims != null ? claims.getExpiracion() : java.time.LocalDateTime.now().plusHours(1);
            tokenRevocationGateway.revocarToken(token, expiracion);
            log.info("Token invalidado y registrado en lista de revocacion");
        }
        return ResponseEntity.ok(Map.of("mensaje", "Sesion cerrada exitosamente y token revocado"));
    }
}
