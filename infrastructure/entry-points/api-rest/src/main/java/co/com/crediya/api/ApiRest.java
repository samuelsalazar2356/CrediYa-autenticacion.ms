package co.com.crediya.api;

import co.com.crediya.api.dto.UsuarioRequestDTO;
import co.com.crediya.api.dto.UsuarioResponseDTO;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiRest {

    private static final Logger log = LoggerFactory.getLogger(ApiRest.class);
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO requestDTO) {
        log.info("Iniciando solicitud para registrar usuario con correo: {}", requestDTO.getCorreoElectronico());

        Usuario usuario = requestDTO.toDomain();
        Usuario usuarioCreado = registrarUsuarioUseCase.registrar(usuario);

        log.info("Usuario registrado exitosamente con ID: {} y estado: {}", usuarioCreado.getId(), usuarioCreado.getEstado());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.exitoso(usuarioCreado));
    }
}
