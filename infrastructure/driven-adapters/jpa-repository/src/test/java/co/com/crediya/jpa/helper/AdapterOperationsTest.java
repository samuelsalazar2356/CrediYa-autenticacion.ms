package co.com.crediya.jpa.helper;

import co.com.crediya.jpa.JPARepository;
import co.com.crediya.jpa.JPARepositoryAdapter;
import co.com.crediya.jpa.UsuarioData;
import co.com.crediya.model.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    @Mock
    private JPARepository repository;

    @Mock
    private ObjectMapper objectMapper;

    private JPARepositoryAdapter adapter;

    private Usuario usuario;
    private UsuarioData usuarioData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder().id(1L).nombres("Juan").correoElectronico("juan@correo.com").build();
        usuarioData = UsuarioData.builder().id(1L).nombres("Juan").correoElectronico("juan@correo.com").build();

        when(objectMapper.map(usuario, UsuarioData.class)).thenReturn(usuarioData);
        when(objectMapper.map(usuarioData, Usuario.class)).thenReturn(usuario);

        adapter = new JPARepositoryAdapter(repository, objectMapper);
    }

    @Test
    void testSave() {
        when(repository.save(usuarioData)).thenReturn(usuarioData);

        Usuario result = adapter.save(usuario);

        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    void testSaveAllEntities() {
        List<Usuario> usuarios = List.of(usuario);
        List<UsuarioData> usuarioDataList = List.of(usuarioData);

        when(repository.saveAll(usuarioDataList)).thenReturn(usuarioDataList);

        List<Usuario> result = adapter.saveAllEntities(usuarios);

        assertEquals(usuarios.size(), result.size());
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioData));

        Usuario result = adapter.findById(1L);

        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    void testFindAll() {
        List<UsuarioData> usuarioDataList = List.of(usuarioData);

        when(repository.findAll()).thenReturn(usuarioDataList);

        List<Usuario> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testFindByExample() {
        List<UsuarioData> usuarioDataList = List.of(usuarioData);

        when(repository.findAll(any(Example.class))).thenReturn(usuarioDataList);

        List<Usuario> result = adapter.findByExample(usuario);

        assertEquals(1, result.size());
    }
}
