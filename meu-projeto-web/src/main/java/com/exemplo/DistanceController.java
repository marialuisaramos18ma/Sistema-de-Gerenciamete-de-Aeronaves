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

import java.text.DecimalFormat;

@Controller
public class DistanceController {

    @Autowired
    private DistanceCalculator distanceCalculator;

    @Autowired
    private AeronaveRepository aeronaveRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @GetMapping("/distancia")
    public String mostrarFormularioDeDistancia(Model model, @RequestParam(defaultValue = "0") int page) {
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

        model.addAttribute("aeronaves", aeronaveRepository.findAll());
        model.addAttribute("aeroportos", aeroportoRepository.findAll());
        return "distancia";
    }

    @PostMapping("/calcularDistancia")
    public String calcularDistancia(@RequestParam Long origemId, @RequestParam Long destinoId,
                                    @RequestParam Long aeronaveId, Model model, @RequestParam(defaultValue = "0") int page) {
        Aeroporto origem = aeroportoRepository.findById(origemId).orElse(null);
        Aeroporto destino = aeroportoRepository.findById(destinoId).orElse(null);

        double distancia = distanceCalculator.calculateDistance(origem.getLatitude(), origem.getLongitude(),
                                                                destino.getLatitude(), destino.getLongitude());

        Aeronave aeronave = aeronaveRepository.findById(aeronaveId).orElse(null);
        double autonomia = aeronave.getAutonomia();

        int paradasNecessarias = autonomia >= distancia ? 0 : (int) Math.ceil(distancia / autonomia);

        model.addAttribute("distancia", df.format(distancia));
        model.addAttribute("autonomia", df.format(autonomia));
        model.addAttribute("paradasNecessarias", paradasNecessarias);
        model.addAttribute("origemNome", origem.getNome());
        model.addAttribute("destinoNome", destino.getNome());
        model.addAttribute("aeronave", aeronave);
        model.addAttribute("result", true);

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

        model.addAttribute("aeronaves", aeronaveRepository.findAll());
        Page<Aeroporto> aeroportosPage = aeroportoRepository.findAll(PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("aeroportos", aeroportosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", aeroportosPage.getTotalPages());

        return "distancia";
    }
}
