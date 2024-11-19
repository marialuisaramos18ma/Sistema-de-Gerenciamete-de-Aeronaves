package com.exemplo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listarUsuarios(Model model) {
        // Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUserName = userDetails.getUsername();
            Usuario usuarioLogado = usuarioRepository.findByEmail(currentUserName);
            model.addAttribute("usuarioLogado", usuarioLogado);
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios"; // Nome do template de listagem
    }

    @GetMapping("/novo")
    public String mostrarFormularioDeCriacaoDeUsuario(Model model) {
        // Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUserName = userDetails.getUsername();
            Usuario usuarioLogado = usuarioRepository.findByEmail(currentUserName);
            model.addAttribute("usuarioLogado", usuarioLogado);
        }

        model.addAttribute("usuario", new Usuario());
        return "criarUsuario"; // Nome do template de criação
    }

    @PostMapping
    public String salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return "criarUsuario";
        }
        // Criptografar a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        return "redirect:/home"; // Redirecionar para a página home
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicaoDeUsuario(@PathVariable("id") Long id, Model model) {
        // Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUserName = userDetails.getUsername();
            Usuario usuarioLogado = usuarioRepository.findByEmail(currentUserName);
            model.addAttribute("usuarioLogado", usuarioLogado);
        }

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        model.addAttribute("usuario", usuario);
        return "editarUsuario"; // Nome do template de edição
    }

    @PostMapping("/editar/{id}")
    public String atualizarUsuario(@PathVariable("id") Long id, @Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return "editarUsuario";
        }
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
        
        // Verificar se a senha foi preenchida
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            usuario.setSenha(usuarioExistente.getSenha());
        } else {
            // Criptografar a nova senha antes de salvar
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        usuario.setId(id);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios"; // Redirecionar para a lista de usuários
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable("id") Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios"; // Redirecionar para a lista de usuários
    }
}
