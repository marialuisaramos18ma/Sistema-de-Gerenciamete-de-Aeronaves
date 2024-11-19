package com.exemplo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void testCreateReadDelete() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@example.com");
        usuario.setSenha("123456");

        // Create
        usuario = usuarioRepository.save(usuario);
        assertThat(usuarioRepository.findById(usuario.getId())).isPresent();

        // Read
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).extracting(Usuario::getEmail).containsOnly("teste@example.com");

        // Delete
        usuarioRepository.deleteById(usuario.getId());
        assertThat(usuarioRepository.findById(usuario.getId())).isNotPresent();
    }

    @Test
    public void testFindByEmail() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste2");
        usuario.setEmail("teste2@example.com");
        usuario.setSenha("654321");

        // Create
        usuario = usuarioRepository.save(usuario);
        assertThat(usuarioRepository.findByEmail("teste2@example.com")).isNotNull();

        // Cleanup
        usuarioRepository.deleteById(usuario.getId());
    }
}
