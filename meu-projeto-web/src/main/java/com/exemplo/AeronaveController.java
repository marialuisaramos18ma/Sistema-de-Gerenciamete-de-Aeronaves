package com.exemplo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/aeronaves")
public class AeronaveController {

    @Autowired
    private AeronaveRepository aeronaveRepository;

    @Autowired
    private ManutencaoRepository manutencaoRepository; // Injeção do repositório de manutenção

    @PostMapping
    public String salvarAeronave(@Valid @ModelAttribute("aeronave") Aeronave aeronave, BindingResult result) {
        if (result.hasErrors()) {
            return "criarAeronave";
        }
        aeronaveRepository.save(aeronave);
        return "redirect:/home";
    }

    @GetMapping("/editar/{id}")
    public String editarAeronave(@PathVariable("id") Long id, Model model) {
        Aeronave aeronave = aeronaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aeronave inválida:" + id));
        model.addAttribute("aeronave", aeronave);
        return "editarAeronave";
    }

    @PostMapping("/editar/{id}")
    public String atualizarAeronave(@PathVariable("id") Long id, @Valid @ModelAttribute("aeronave") Aeronave aeronave, BindingResult result) {
        if (result.hasErrors()) {
            return "editarAeronave";
        }
        aeronave.setId(id);
        aeronaveRepository.save(aeronave);
        return "redirect:/home";
    }

    @GetMapping("/excluir/{id}")
    public String excluirAeronave(@PathVariable("id") Long id, Model model) {
        try {
            Aeronave aeronave = aeronaveRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Aeronave inválida:" + id));
            // Excluir dados de manutenção associados
            List<Manutencao> manutencoes = manutencaoRepository.findByAeronave(aeronave);
            if (!manutencoes.isEmpty()) {
                manutencaoRepository.deleteAll(manutencoes);
            }
            aeronaveRepository.delete(aeronave);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Não foi possível excluir a aeronave: " + e.getMessage());
            return "erroExcluirAeronave";
        }
        return "redirect:/home";
    }
}
