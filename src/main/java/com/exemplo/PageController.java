package com.exemplo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute; // Adicionando a importação de ModelAttribute

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AeronaveRepository aeronaveRepository;

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @GetMapping("/")
    public String showLoginPage() {
        return "login"; // Redirecionar para a página de login
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            currentUserName = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        Usuario usuario = usuarioRepository.findByEmail(currentUserName);
        model.addAttribute("usuario", usuario);

        // Adicionando a lista de aeronaves ao modelo com o estado de manutenção
        List<Aeronave> aeronaves = aeronaveRepository.findAll();
        List<AeronaveComStatus> aeronavesComStatus = aeronaves.stream().map(aeronave -> {
            String status = verificarEstadoManutencao(aeronave);
            return new AeronaveComStatus(aeronave, status);
        }).collect(Collectors.toList());

        model.addAttribute("aeronavesComStatus", aeronavesComStatus);

        return "home";
    }

    @GetMapping("/todos-usuarios")
    public String listarTodosUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios"; // Use o template "usuarios"
    }

    @GetMapping("/aeronave/novo")
    public String mostrarFormularioDeCriacaoDeAeronave(Model model) {
        model.addAttribute("aeronave", new Aeronave());
        return "criarAeronave"; // Nome do template de criação de aeronave
    }

    @GetMapping("/aeronave/editar")
    public String mostrarFormularioDeEdicaoDeAeronave(@RequestParam Long id, Model model) {
        Aeronave aeronave = aeronaveRepository.findById(id).orElse(null);
        if (aeronave != null) {
            model.addAttribute("aeronave", aeronave);
            return "editarAeronave"; // Nome do template de edição de aeronave
        }
        return "redirect:/home";
    }

    @PostMapping("/aeronave/atualizar")
    public String atualizarAeronave(@ModelAttribute Aeronave aeronave) {
        aeronaveRepository.save(aeronave);
        return "redirect:/home";
    }

    @GetMapping("/aeronave/excluir")
    public String excluirAeronave(@RequestParam Long id) {
        Aeronave aeronave = aeronaveRepository.findById(id).orElse(null);
        if (aeronave != null) {
            // Excluir dados de manutenção associados
            List<Manutencao> manutencoes = manutencaoRepository.findByAeronave(aeronave);
            if (manutencoes != null && !manutencoes.isEmpty()) {
                manutencaoRepository.deleteAll(manutencoes);
            }
            // Excluir a aeronave
            aeronaveRepository.delete(aeronave);
        }
        return "redirect:/home";
    }

    @GetMapping("/calculadora")
    public String mostrarFormularioCalculadora(Model model) throws Exception {
        List<Aeronave> aeronaves = aeronaveRepository.findAll();
        model.addAttribute("aeronaves", aeronaves);
        model.addAttribute("aeronavesJson", new ObjectMapper().writeValueAsString(aeronaves));
        return "calculadora"; // Nome do template da calculadora
    }

    @PostMapping("/calcularAutonomia")
    public String calcularAutonomia(@RequestParam Long aeronaveId, @RequestParam double peso, @RequestParam double altitudeVoo, Model model) {
        Aeronave aeronave = aeronaveRepository.findById(aeronaveId).orElse(null);
        if (aeronave != null) {
            // Fórmula fictícia para calcular a autonomia
            double autonomia = (aeronave.getCapacidadeTanque() * 10) / (aeronave.getQuantidadeMotores() * (peso / 1000) * (altitudeVoo / 1000));
            aeronave.setPeso(peso);
            aeronave.setAltitudeVoo(altitudeVoo);
            aeronave.setAutonomia(autonomia);
            aeronaveRepository.save(aeronave);
        }
        return "redirect:/home";
    }

    // Método para verificar o estado de manutenção de uma aeronave
    public String verificarEstadoManutencao(Aeronave aeronave) {
        List<Manutencao> manutencoes = manutencaoRepository.findByAeronave(aeronave);
        if (manutencoes != null && !manutencoes.isEmpty()) {
            for (Manutencao manutencao : manutencoes) {
                if (!(manutencao.isBlindagemAeronave() &&
                        manutencao.isCabineCentralPiloto() &&
                        manutencao.isMotoresTurbinas() &&
                        manutencao.isAssentosCintosSeguranca() &&
                        manutencao.isTremPouso())) {
                    return "✘"; // Manutenção pendente
                }
            }
            return "✔"; // Manutenção em dia
        }
        return "✘"; // Sem manutenção registrada
    }

    private static class AeronaveComStatus {
        private Aeronave aeronave;
        private String status;

        public AeronaveComStatus(Aeronave aeronave, String status) {
            this.aeronave = aeronave;
            this.status = status;
        }

        // Getters e Setters
        public Aeronave getAeronave() {
            return aeronave;
        }

        public void setAeronave(Aeronave aeronave) {
            this.aeronave = aeronave;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
