package co.com.crediya.api;

import co.com.crediya.api.dto.LoginRequestDTO;
import co.com.crediya.api.dto.LoginResponseDTO;
import co.com.crediya.model.usuario.SesionUsuario;
import co.com.crediya.usecase.autenticacion.AutenticarUsuarioUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para el inicio de sesion y expedicion de tokens de autenticacion.
 * Expone el endpoint POST /api/v1/login.
 */
@RestController
@RequestMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginApiRest {

    private static final Logger log = LoggerFactory.getLogger(LoginApiRest.class);
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    public LoginApiRest(AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
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
}
