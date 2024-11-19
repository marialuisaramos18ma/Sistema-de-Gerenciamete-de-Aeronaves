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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/manutencao")
public class ManutencaoController {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private AeronaveRepository aeronaveRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String showManutencaoForm(Model model, @RequestParam(defaultValue = "0") int page) {
        // Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            currentUserName = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            currentUserName = authentication.getPrincipal().toString();
        }
        Usuario usuarioLogado = usuarioRepository.findByEmail(currentUserName);
        model.addAttribute("usuarioLogado", usuarioLogado);

        model.addAttribute("manutencao", new Manutencao());
        model.addAttribute("aeronaves", aeronaveRepository.findAll());

        Page<Manutencao> manutencoesPage = manutencaoRepository.findAll(PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("manutencoes", manutencoesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", manutencoesPage.getTotalPages());
        return "manutencao";
    }

    @PostMapping("/salvar")
    public String salvarManutencao(@Valid @ModelAttribute("manutencao") Manutencao manutencao, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("aeronaves", aeronaveRepository.findAll());
            model.addAttribute("manutencoes", manutencaoRepository.findAll());
            return "manutencao";
        }

        // Obter o nome do usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            currentUserName = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            currentUserName = authentication.getPrincipal().toString();
        }
        Usuario usuario = usuarioRepository.findByEmail(currentUserName);
        manutencao.setNomeUsuario(usuario.getNome());

        // Verificar se já existe uma manutenção para a aeronave
        List<Manutencao> existingManutencoes = manutencaoRepository.findByAeronave(manutencao.getAeronave());
        if (!existingManutencoes.isEmpty()) {
            Manutencao manutencaoToUpdate = existingManutencoes.get(0);
            manutencaoToUpdate.setBlindagemAeronave(manutencao.isBlindagemAeronave());
            manutencaoToUpdate.setCabineCentralPiloto(manutencao.isCabineCentralPiloto());
            manutencaoToUpdate.setMotoresTurbinas(manutencao.isMotoresTurbinas());
            manutencaoToUpdate.setAssentosCintosSeguranca(manutencao.isAssentosCintosSeguranca());
            manutencaoToUpdate.setTremPouso(manutencao.isTremPouso());
            manutencaoToUpdate.setDataAtualizacao(LocalDateTime.now());
            manutencaoRepository.save(manutencaoToUpdate);
        } else {
            manutencao.setDataAtualizacao(LocalDateTime.now()); // Definir automaticamente a data de atualização
            manutencaoRepository.save(manutencao);
        }

        return "redirect:/manutencao"; // Permanecer na mesma página
    }

    @GetMapping("/editar/{id}")
    public String editarManutencao(@PathVariable("id") Long id, Model model) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção inválida:" + id));
        model.addAttribute("manutencao", manutencao);
        model.addAttribute("aeronaves", aeronaveRepository.findAll());
        return "editarManutencao";
    }

    @PostMapping("/editar/{id}")
    public String atualizarManutencao(@PathVariable("id") Long id, @Valid @ModelAttribute("manutencao") Manutencao manutencao, BindingResult result) {
        if (result.hasErrors()) {
            return "editarManutencao";
        }
        manutencao.setId(id);
        manutencao.setDataAtualizacao(LocalDateTime.now()); // Atualizar a data de atualização
        manutencaoRepository.save(manutencao);
        return "redirect:/manutencao"; // Permanecer na mesma página
    }

    @GetMapping("/excluir/{id}")
    public String excluirManutencao(@PathVariable("id") Long id, Model model) {
        try {
            Manutencao manutencao = manutencaoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Manutenção inválida:" + id));
            manutencaoRepository.delete(manutencao);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Não foi possível excluir a manutenção: " + e.getMessage());
            return "erroExcluirManutencao";
        }
        return "redirect:/manutencao";
    }
}
