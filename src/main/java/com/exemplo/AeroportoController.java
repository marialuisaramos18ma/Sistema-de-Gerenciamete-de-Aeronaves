package com.exemplo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AeroportoController {

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/aeroporto/novo")
    public String mostrarFormularioDeCriacaoDeAeroporto(Model model, @RequestParam(defaultValue = "0") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            currentUserName = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            currentUserName = authentication.getPrincipal().toString();
        }
        Usuario usuarioLogado = usuarioRepository.findByEmail(currentUserName);
        model.addAttribute("usuarioLogado", usuarioLogado);

        model.addAttribute("aeroporto", new Aeroporto());
        Page<Aeroporto> aeroportosPage = aeroportoRepository.findAll(PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("aeroportos", aeroportosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", aeroportosPage.getTotalPages());
        return "criarAeroporto"; // Nome do template de criação de aeroporto
    }

    @PostMapping("/salvarAeroporto")
    public String salvarAeroporto(@ModelAttribute Aeroporto aeroporto) {
        aeroportoRepository.save(aeroporto);
        return "redirect:/aeroporto/novo"; // Redirecionar para a lista de aeroportos
    }

    @PostMapping("/aeroporto/deletar/{id}")
    public String deletarAeroporto(@PathVariable("id") Long id) {
        aeroportoRepository.deleteById(id);
        return "redirect:/aeroporto/novo"; // Redirecionar para a lista de aeroportos
    }
}
