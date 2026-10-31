package co.com.crediya.jpa;

import co.com.crediya.jpa.helper.AdapterOperations;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<Usuario, UsuarioData, Long, JPARepository>
        implements UsuarioRepository {

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return super.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCorreoElectronico(String correoElectronico) {
        return repository.existsByCorreoElectronico(correoElectronico);
    }
}