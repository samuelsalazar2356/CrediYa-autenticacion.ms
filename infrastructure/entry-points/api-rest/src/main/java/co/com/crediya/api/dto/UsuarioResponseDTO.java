package co.com.crediya.api.dto;

import co.com.crediya.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private String mensaje;
    private Usuario usuario;
    private LocalDateTime timestamp;

    public static UsuarioResponseDTO exitoso(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .mensaje("Usuario registrado exitosamente en el sistema CrediYa")
                .usuario(usuario)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
