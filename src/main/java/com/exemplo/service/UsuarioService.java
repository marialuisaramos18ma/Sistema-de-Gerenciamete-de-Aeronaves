package com.exemplo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(UsuarioService.class.getName());

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            LOGGER.warning("Usuário não encontrado com email: " + email);
            throw new UsernameNotFoundException("Usuário não encontrado com email: " + email);
        }
        LOGGER.info("Usuário encontrado: " + usuario.getEmail());
        LOGGER.info("Senha criptografada no banco de dados: " + usuario.getSenha());
        return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }
}
